package com.eventdrivenmicroservices.orderservice.saga;

import com.eventdrivenmicroservices.edmcore.command.CancelProductReservationCommand;
import com.eventdrivenmicroservices.edmcore.command.ProcessPaymentCommand;
import com.eventdrivenmicroservices.edmcore.command.ReserveProductCommand;
import com.eventdrivenmicroservices.edmcore.event.PaymentProcessedEvent;
import com.eventdrivenmicroservices.edmcore.event.ProductReservationCancelledEvent;
import com.eventdrivenmicroservices.edmcore.event.ProductReservedEvent;
import com.eventdrivenmicroservices.edmcore.model.User;
import com.eventdrivenmicroservices.edmcore.query.FetchUserPaymentDetailsQuery;
import com.eventdrivenmicroservices.orderservice.command.ApprovedOrderCommand;
import com.eventdrivenmicroservices.orderservice.command.OrderRejectedEvent;
import com.eventdrivenmicroservices.orderservice.command.event.OrderApprovedEvent;
import com.eventdrivenmicroservices.orderservice.command.event.OrderCreatedEvent;
import com.eventdrivenmicroservices.orderservice.core.OrderSummary;
import com.eventdrivenmicroservices.orderservice.query.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private transient DeadlineManager deadlineManager;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    private final String PAYMENT_PROCESSING_DEADLINE = "payment-processing-deadline";

    private String scheduleId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        ReserveProductCommand command = ReserveProductCommand.builder()
            .orderId(event.orderId)
            .productId(event.getProductId())
            .quantity(event.getQuantity())
            .userId(event.getUserId())
            .build();

        log.info("OrderCreatedEvent handled for orderId: " + command.getOrderId() + " and productId: " +
                 command.getProductId());

        commandGateway.send(command, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                RejectedOrderCommand rejectedOrderCommand = new RejectedOrderCommand(event.getOrderId(),
                    commandResultMessage.exceptionResult().getMessage());

                commandGateway.send(rejectedOrderCommand);
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {
        log.info("ProductReservedEvent is called for productId: " + event.getProductId() +
                 " and orderId: " + event.getOrderId());

        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery =
            new FetchUserPaymentDetailsQuery(event.getUserId());

        User userPaymentDetails = null;

        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class))
                .join();
        } catch (Exception ex){
           log.error(ex.getMessage());
            cancelProductReservation(event, ex.getMessage());
            return;
        }

        if (userPaymentDetails == null) {
            cancelProductReservation(event, "Could not fetch user payment details");
            return;
        }

        log.info("Successfully fetched user payment details for user " + userPaymentDetails.getFirstName());

        scheduleId = deadlineManager.schedule(Duration.of(120, ChronoUnit.SECONDS), PAYMENT_PROCESSING_DEADLINE, event);

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
            .orderId(event.getOrderId())
            .paymentDetails(userPaymentDetails.getPaymentDetails())
            .paymentId(UUID.randomUUID().toString())
            .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            cancelProductReservation(event, ex.getMessage());
            return;
        }

        if (result == null) {
            log.info("The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction");
            cancelProductReservation(event, "Could not process user payment with provided payment " +
                                                           "details");
        }
    }

    private void cancelProductReservation(ProductReservedEvent event, String reason) {

        cancelDeadline();

        CancelProductReservationCommand command =
            CancelProductReservationCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(command);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event) {
        cancelDeadline();
        ApprovedOrderCommand command = new ApprovedOrderCommand(event.getOrderId());
        commandGateway.send(command);
    }

    public void cancelDeadline() {
        if (scheduleId != null) {
            deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId);
            scheduleId = null;
        }
    }
    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent event) {
        log.info("Order is approved. Order Saga is complete for orderId" + event.getOrderId());
        //SagaLifecycle.end();
        queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, new OrderSummary(event.getOrderId(),
            event.getOrderStatus(), ""));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent event) {
        // create and send a RejectOrderCommand
        RejectedOrderCommand command = new RejectedOrderCommand(event.getOrderId(), event.getReason());
        commandGateway.send(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent event) {
        log.info("Successfully rejected order with id " + event.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, new OrderSummary(event.getOrderId(),
            event.getOrderStatus(), event.getReason()));
    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
    public void handlePaymentDetails(ProductReservedEvent event) {
        log.info("Payment processing deadline took place. Sending a compensating command to cancel the product " +
                 "reservation");
        cancelProductReservation(event, "Payment timeout");
    }
}
