package com.eventdrivenmicroservices.paymentservice.core.persistence.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="payment")
@Data
public class PaymentEntity implements Serializable {
    @Id
    private String paymentId;
    @Column
    public String orderId;
}
