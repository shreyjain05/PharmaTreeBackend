package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name="product_inventory")
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String HSNCode;
    private String batchNumber;
    private Date expiryDate;
    private String basicRate;
    private String mrp;
    private String purchaseRate;
    private String saleRate;
    private String discount;
    private String inventoryCount;
    private String inventoryUpdateId;
    private boolean isActive=true;
    private String createdBy ="ADMIN";
    private String updatedBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;
}
