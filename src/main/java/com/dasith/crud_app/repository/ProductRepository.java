package com.dasith.crud_app.repository;

import com.dasith.crud_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
