package com.eventdrivenmicroservices.orderservice.command.rest;

import com.eventdrivenmicroservices.orderservice.command.CreateOrderCommand;
import com.eventdrivenmicroservices.orderservice.command.rest.dto.CreateOrderDto;
import com.eventdrivenmicroservices.orderservice.core.OrderSummary;
import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderStatus;
import com.eventdrivenmicroservices.orderservice.query.FindOrderQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrderCommandController(
        CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public OrderSummary createOrder(@Valid
                                @RequestBody
                                  CreateOrderDto createOrderDto) {

        String orderId = UUID.randomUUID().toString();
        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";

        CreateOrderCommand command = CreateOrderCommand.builder()
            .orderStatus(OrderStatus.CREATED)
            .addressId(createOrderDto.getAddressId())
            .quantity(createOrderDto.getQuantity())
            .productId(createOrderDto.getProductId())
            .userId(userId)
            .orderId(orderId)
            .build();

        SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult =
            queryGateway.subscriptionQuery(new FindOrderQuery(orderId),
            ResponseTypes.instanceOf(OrderSummary.class),
            ResponseTypes.instanceOf(OrderSummary.class));

        try {
            commandGateway.sendAndWait(command);
            return queryResult.updates().blockFirst();
        } finally {
            queryResult.close();
        }
    }
}
