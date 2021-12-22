package com.eventdrivenmicroservices.productservice.core.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_lookup")
@Data
public class ProductLookupEntity {
    @Id
    @Column(unique = true)
    private String productId;

    @Column(unique = true)
    private String title;
}