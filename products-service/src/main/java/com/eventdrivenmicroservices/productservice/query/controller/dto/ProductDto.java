package com.eventdrivenmicroservices.productservice.query.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
