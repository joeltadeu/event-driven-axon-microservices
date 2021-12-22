package com.eventdrivenmicroservices.productservice.command.handler;

import com.eventdrivenmicroservices.productservice.command.event.ProductCreatedEvent;
import com.eventdrivenmicroservices.productservice.core.persistence.entity.ProductLookupEntity;
import com.eventdrivenmicroservices.productservice.core.persistence.repository.ProductLookupRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventHandler {

    private final ProductLookupRepository repository;

    public ProductLookupEventHandler(
        ProductLookupRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductLookupEntity entity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        repository.save(entity);
    }

    @ResetHandler
    public void reset() {
        repository.deleteAll();
    }
}
