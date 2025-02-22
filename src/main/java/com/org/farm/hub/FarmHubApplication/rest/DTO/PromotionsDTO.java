package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PromotionsDTO {

    private Long id;
    private String name;
    private String description;
    private String status;
    private Date startDate;
    private Date endDate;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private PromotionDetailsDTO promotionDetails;
}
