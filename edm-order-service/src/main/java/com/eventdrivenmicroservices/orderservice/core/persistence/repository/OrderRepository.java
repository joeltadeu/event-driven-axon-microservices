package com.eventdrivenmicroservices.orderservice.core.persistence.repository;

import com.eventdrivenmicroservices.orderservice.core.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    OrderEntity findByOrderId(String orderId);
}
