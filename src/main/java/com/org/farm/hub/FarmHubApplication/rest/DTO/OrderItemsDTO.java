package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItemsDTO {

    private Long id;
    private String productName;
    private String productID;
    private String productCode;
    private String batch;
    private String quantity;
    private String billAmount;
    private String itemCGST;
    private String itemSGST;
    private String itemBill;
    private String discount;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
