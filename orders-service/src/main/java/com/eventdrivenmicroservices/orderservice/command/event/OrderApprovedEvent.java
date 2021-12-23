package com.eventdrivenmicroservices.orderservice.command.event;

import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
