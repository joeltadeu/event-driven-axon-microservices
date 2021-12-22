package com.eventdrivenmicroservices.edmcore.command;

import com.eventdrivenmicroservices.edmcore.model.PaymentDetails;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class ProcessPaymentCommand {
    @TargetAggregateIdentifier
    private final String paymentId;

    private final String orderId;
    private final PaymentDetails paymentDetails;
}
