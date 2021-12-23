package com.eventdrivenmicroservices.orderservice.command.rest.dto;

import lombok.Data;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class CreateOrderDto {
    @NotBlank(message = "Order productId is a required field")
    private String productId;

    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Max(value = 5, message = "Quantity cannot be larger than 5")
    private int quantity;

    @NotBlank(message = "Order addressId is a required field")
    private String addressId;
}
