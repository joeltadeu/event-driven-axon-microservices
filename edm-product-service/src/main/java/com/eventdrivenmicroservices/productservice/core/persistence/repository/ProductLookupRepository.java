package com.eventdrivenmicroservices.productservice.core.persistence.repository;

import com.eventdrivenmicroservices.productservice.core.persistence.entity.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
