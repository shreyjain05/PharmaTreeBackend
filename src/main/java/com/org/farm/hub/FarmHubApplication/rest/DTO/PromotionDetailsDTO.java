package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PromotionDetailsDTO {

    private Long id;
    private String productName;
    private String productID;
    private String buyQuantity;
    private String freeQuantity;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
