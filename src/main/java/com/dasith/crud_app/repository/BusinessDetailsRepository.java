package com.dasith.crud_app.repository;

import com.dasith.crud_app.model.BusinessDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessDetailsRepository extends JpaRepository<BusinessDetails,Long> {
}
