package com.romb.rombApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
    private double weight;
    private double calories;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private MenuCategory category;
}





