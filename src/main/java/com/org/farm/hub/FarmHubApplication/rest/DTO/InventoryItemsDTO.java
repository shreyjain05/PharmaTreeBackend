package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class InventoryItemsDTO {

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
