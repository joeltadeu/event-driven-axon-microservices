package com.eventdrivenmicroservices.orderservice.command.handler;

import com.eventdrivenmicroservices.orderservice.command.OrderRejectedEvent;
import com.eventdrivenmicroservices.orderservice.command.event.OrderApprovedEvent;
import com.eventdrivenmicroservices.orderservice.command.event.OrderCreatedEvent;
import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderEntity;
import com.eventdrivenmicroservices.orderservice.core.persistence.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrderRepository repository;

    public OrderEventHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);
        repository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        OrderEntity orderEntity = repository.findByOrderId(event.getOrderId());

        if (orderEntity == null) {
            return;
        }

        orderEntity.setOrderStatus(event.getOrderStatus());
        repository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        OrderEntity orderEntity = repository.findByOrderId(event.getOrderId());
        orderEntity.setOrderStatus(event.getOrderStatus());
        repository.save(orderEntity);
    }
}
