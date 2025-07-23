package com.dasith.crud_app.service;

import com.dasith.crud_app.model.Business;
import com.dasith.crud_app.model.User;
import com.dasith.crud_app.repository.BusinessRepository;
import com.dasith.crud_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Business> getAllBusiness(){
        return businessRepository.findAll();
    }

    public Optional<Business> getBusinessById(Long id) {
        return businessRepository.findById(id);
    }

    public Business createBusinessForUser(Long userId ,Business business) {
        // Find the user by their ID.
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found with id"+userId));
        //Associate the business with the found user
        business.setUser(user);

        // The repository handles saving and updating the relationship.
        return businessRepository.save(business);
    }

    public Business updateBusiness(Long id, Business newBusiness) {
        Business business =businessRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Business not found with id"+ id));

        business.setName(newBusiness.getName());
        business.setType(newBusiness.getType());
        business.setIndustry(newBusiness.getIndustry());
        business.setDescription(newBusiness.getDescription());
        return businessRepository.save(business);
    }

    public void deteleBusiness(Long id) {
        businessRepository.deleteById(id);
    }
}
