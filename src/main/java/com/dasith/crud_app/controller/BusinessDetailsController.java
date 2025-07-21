package com.dasith.crud_app.controller;

import com.dasith.crud_app.model.BusinessDetails;

import com.dasith.crud_app.service.BusinessDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
@CrossOrigin(origins = "*")
public class BusinessDetailsController {
    @Autowired
    private BusinessDetailsService businessDetailsService;

    @GetMapping
    public List<BusinessDetails> getBusinessDetails(){
        return businessDetailsService.getAllBusinessDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDetails> getgetBusinessDetailsById(@PathVariable Long id){
        return  businessDetailsService.getBusinessDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BusinessDetails createBusinessDetails(@RequestBody BusinessDetails businessDetails){
        return businessDetailsService.createBusiness(businessDetails);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BusinessDetails> updateBusinessDeatils(@PathVariable Long id,@RequestBody BusinessDetails businessDetails){
        try{
            return ResponseEntity.ok(businessDetailsService.updateBusinessDeatils(id,businessDetails));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessDetails(@PathVariable Long id){
        businessDetailsService.deteleBusinessDetails(id);
        return ResponseEntity.noContent().build();
    }

}
