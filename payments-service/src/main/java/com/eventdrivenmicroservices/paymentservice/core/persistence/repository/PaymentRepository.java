package com.eventdrivenmicroservices.paymentservice.core.persistence.repository;

import com.eventdrivenmicroservices.paymentservice.core.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}
