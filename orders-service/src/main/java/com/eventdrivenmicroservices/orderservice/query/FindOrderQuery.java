package com.eventdrivenmicroservices.orderservice.query;

import lombok.Value;

@Value
public class FindOrderQuery {
    private final String orderId;
}
