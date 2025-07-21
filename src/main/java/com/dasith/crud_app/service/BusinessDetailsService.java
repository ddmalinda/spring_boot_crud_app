package com.dasith.crud_app.service;

import com.dasith.crud_app.model.BusinessDetails;
import com.dasith.crud_app.repository.BusinessDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessDetailsService {
    @Autowired
    private BusinessDetailsRepository businessDetailsRepository;

    public List<BusinessDetails> getAllBusinessDetails(){
        return businessDetailsRepository.findAll();
    }

    public Optional<BusinessDetails> getBusinessDetailsById(Long id) {
        return businessDetailsRepository.findById(id);
    }

    public BusinessDetails createBusiness(BusinessDetails businessDetails) {
        return businessDetailsRepository.save(businessDetails);
    }

    public BusinessDetails updateBusinessDeatils(Long id, BusinessDetails businessDetails) {
        BusinessDetails business =businessDetailsRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Business not found with id"+ id));

        business.setName(business.getName());
        business.setType(business.getType());
        business.setIndustry(business.getIndustry());
        business.setDescription(business.getDescription());
        return businessDetailsRepository.save(business);
    }

    public void deteleBusinessDetails(Long id) {
        businessDetailsRepository.deleteById(id);
    }
}
