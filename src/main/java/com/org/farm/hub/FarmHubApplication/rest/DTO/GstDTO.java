package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GstDTO {

    private Long id;
    private String GSTNumber;
    private String GSTStateCode;
    private String status;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
