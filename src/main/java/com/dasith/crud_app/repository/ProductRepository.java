package com.dasith.crud_app.repository;

import com.dasith.crud_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBusinessId(Long businessId);
}
