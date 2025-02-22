package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name="promotions_details")
public class PromotionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productID;
    private String buyQuantity;
    private String freeQuantity;
    private String createdBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "promotion_id")
    private Promotions promotions;
}
