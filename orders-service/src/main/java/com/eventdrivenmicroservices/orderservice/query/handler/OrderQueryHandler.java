package com.eventdrivenmicroservices.orderservice.query.handler;

import com.eventdrivenmicroservices.orderservice.core.OrderSummary;
import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderEntity;
import com.eventdrivenmicroservices.orderservice.core.persistence.repository.OrderRepository;
import com.eventdrivenmicroservices.orderservice.query.FindOrderQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryHandler {

    final OrderRepository repository;

    public OrderQueryHandler(
        OrderRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery query) {
        OrderEntity entity = repository.findByOrderId(query.getOrderId());
        return new OrderSummary(entity.getOrderId(), entity.getOrderStatus(), "");
    }
}
