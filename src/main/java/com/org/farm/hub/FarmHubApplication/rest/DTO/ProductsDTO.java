package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductsDTO {

    private Long id;
    private String name;
    private String description;
    private String composition;
    private String packingName;
    private String companyName;
    private String status;
    private String image;
    private String discount;
    private boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String productCode;

    private List<ProductInventoryDTO> productInventoryList;
}
