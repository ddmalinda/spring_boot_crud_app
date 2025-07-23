package com.dasith.crud_app.controller;

import com.dasith.crud_app.model.Business;

import com.dasith.crud_app.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/businesses")
@CrossOrigin(origins = "*")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @GetMapping
    public List<Business> getBusiness(){
        return businessService.getAllBusiness();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Business> getBusinessById(@PathVariable Long id){
        return  businessService.getBusinessById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Business> createBusiness(@PathVariable Long userId, @RequestBody Business business){
        Business createdBusiness = businessService.createBusinessForUser(userId,business);
        return ResponseEntity.ok(createdBusiness);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Business> updateBusiness(@PathVariable Long id, @RequestBody Business business){
        try{
            return ResponseEntity.ok(businessService.updateBusiness(id, business));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessDetails(@PathVariable Long id){
        businessService.deteleBusiness(id);
        return ResponseEntity.noContent().build();
    }

}
