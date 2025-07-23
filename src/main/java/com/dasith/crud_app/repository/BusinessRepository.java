package com.dasith.crud_app.repository;

import com.dasith.crud_app.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business,Long> {
}
