package com.dasith.crud_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Lombok annotation to generate getters, setters, toString, etc.
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String  category;
    private Integer stock;

    // Many products belong to one business
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private BusinessDetails business;
    
}
