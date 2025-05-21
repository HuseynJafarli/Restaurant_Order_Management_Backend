package com.romb.rombApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableUrl;
    

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Order> orders;
}