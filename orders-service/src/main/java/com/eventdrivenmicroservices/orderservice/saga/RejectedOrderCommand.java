package com.eventdrivenmicroservices.orderservice.saga;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class RejectedOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;

    private final String reason;
}
