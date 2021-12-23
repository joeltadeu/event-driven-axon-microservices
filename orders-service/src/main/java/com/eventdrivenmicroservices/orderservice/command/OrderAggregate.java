package com.eventdrivenmicroservices.orderservice.command;

import com.eventdrivenmicroservices.orderservice.command.event.OrderApprovedEvent;
import com.eventdrivenmicroservices.orderservice.command.event.OrderCreatedEvent;
import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderStatus;
import com.eventdrivenmicroservices.orderservice.saga.RejectedOrderCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApprovedOrderCommand command) {
        OrderApprovedEvent event = new OrderApprovedEvent(command.getOrderId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void on(OrderApprovedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    protected void handle(RejectedOrderCommand command) {
        OrderRejectedEvent event = new OrderRejectedEvent(command.getOrderId(), command.getReason());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void on(OrderRejectedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
