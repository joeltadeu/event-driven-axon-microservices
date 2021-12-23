package com.eventdrivenmicroservices.productservice.query.controller;

import com.eventdrivenmicroservices.productservice.query.FindProductsQuery;
import com.eventdrivenmicroservices.productservice.query.controller.dto.ProductDto;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    private final QueryGateway gateway;

    public ProductQueryController(QueryGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        FindProductsQuery findProductsQuery = new FindProductsQuery();
        List<ProductDto> products = gateway.query(findProductsQuery,
            ResponseTypes.multipleInstancesOf(ProductDto.class)).join();

        return products;
    }
}
