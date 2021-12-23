package com.eventdrivenmicroservices.paymentservice.command.handler;

import com.eventdrivenmicroservices.edmcore.event.PaymentProcessedEvent;
import com.eventdrivenmicroservices.paymentservice.core.persistence.entity.PaymentEntity;
import com.eventdrivenmicroservices.paymentservice.core.persistence.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventHandler {
    private final PaymentRepository paymentRepository;

    public PaymentEventHandler(
        PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent is called for orderId: " + event.getOrderId());

        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(event, paymentEntity);

        paymentRepository.save(paymentEntity);
    }
}
