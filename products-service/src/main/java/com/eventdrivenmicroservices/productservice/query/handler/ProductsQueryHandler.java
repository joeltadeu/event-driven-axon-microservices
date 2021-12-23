package com.eventdrivenmicroservices.productservice.query.handler;

import com.eventdrivenmicroservices.productservice.core.persistence.entity.ProductEntity;
import com.eventdrivenmicroservices.productservice.core.persistence.repository.ProductRepository;
import com.eventdrivenmicroservices.productservice.query.FindProductsQuery;
import com.eventdrivenmicroservices.productservice.query.controller.dto.ProductDto;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {

    private final ProductRepository repository;

    public ProductsQueryHandler(
        ProductRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public List<ProductDto> findProducts(FindProductsQuery query) {
       List<ProductDto> products = new ArrayList<>();
       List<ProductEntity> storedProducts = repository.findAll();

       for (ProductEntity entity : storedProducts) {
           ProductDto productDto = new ProductDto();
           BeanUtils.copyProperties(entity, productDto);
           products.add(productDto);
       }
        return products;
    }
}
