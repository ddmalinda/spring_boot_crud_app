package com.dasith.crud_app.repository;

import com.dasith.crud_app.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBusinessId(Long businessId);

    @Modifying
    @Transactional
    void deleteProductByBusinessId(Long businessId);
}
