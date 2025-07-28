package com.dasith.crud_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Lombok annotation to generate getters, setters, toString, etc.
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String  category;
    private Integer stock;

    // Many Products can belong to one Business.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id") // Foreign key column.
    @JsonBackReference
    private Business business;
    
}
