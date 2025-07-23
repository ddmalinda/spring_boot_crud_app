package com.dasith.crud_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ai_agents")
public class AIAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String agentName;
    private String agentPersonality; // e.g., "friendly", "professional"

    // One AI Agent belongs to one Business.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id") // Foreign key column.
    private Business business;
}