package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name ="customer_royalty")
@Getter
@Setter
public class RoyaltyPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String updatedPoints;
    private String currentBalance;
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();

    @OneToOne(mappedBy = "royaltyPoints")
    private Customer customer;
}
