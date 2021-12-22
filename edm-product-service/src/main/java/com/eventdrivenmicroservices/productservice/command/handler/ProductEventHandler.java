package com.eventdrivenmicroservices.productservice.command.handler;

import com.eventdrivenmicroservices.edmcore.event.ProductReservationCancelledEvent;
import com.eventdrivenmicroservices.edmcore.event.ProductReservedEvent;
import com.eventdrivenmicroservices.productservice.command.event.ProductCreatedEvent;
import com.eventdrivenmicroservices.productservice.core.persistence.entity.ProductEntity;
import com.eventdrivenmicroservices.productservice.core.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@Slf4j
public class ProductEventHandler {

    private final ProductRepository repository;

    public ProductEventHandler(ProductRepository repository) {
        this.repository = repository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);

        try {
            repository.save(productEntity);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        ProductEntity productEntity = repository.findByProductId(event.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - event.getQuantity());
        repository.save(productEntity);
        log.info("ProductReservedEvent is called for productId: " + event.getProductId() + " and orderId: " + event.getOrderId());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event) {
        ProductEntity productEntity = repository.findByProductId(event.getProductId());
        int newQuantity = productEntity.getQuantity() + productEntity.getQuantity();
        productEntity.setQuantity(newQuantity);
        repository.save(productEntity);
        log.info("ProductReservationCancelledEvent is called for productId: " + event.getProductId() + " and orderId:" +
                 " " + event.getOrderId() + ", quantity: " + newQuantity);

    }

    @ResetHandler
    public void reset() {
        repository.deleteAll();
    }
}
