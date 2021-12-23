package com.eventdrivenmicroservices.orderservice.command;

import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
