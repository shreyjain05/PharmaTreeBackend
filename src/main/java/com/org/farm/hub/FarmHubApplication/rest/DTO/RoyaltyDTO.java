package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoyaltyDTO {

    private Long id;

    private String updatedPoints;
    private String currentBalance;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
