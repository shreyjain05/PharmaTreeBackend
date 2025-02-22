package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name="product_inventory_items")
public class InventoryItems {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productId;
    private String batchNumber;
    private Date expiryDate;
    private String basicRate;
    private String mrp;
    private String purchaseRate;
    private String saleRate;
    private String discount;
    private String inventoryCount;
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private InventoryUpdates inventoryUpdates;
}
