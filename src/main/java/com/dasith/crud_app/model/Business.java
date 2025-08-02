package com.dasith.crud_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data

@Table(name = "businesses")
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String industry;
    private String description;

    // Many Businesses can belong to one User.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Creates a 'user_id' foreign key column in this table.
    @JsonBackReference
    private User user;

    // One Business has many Products.
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;

    // One Business has one AI Agent.
    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL)
    private AIAgent agent;
}
