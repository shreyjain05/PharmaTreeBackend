package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerAddressDTO {
    private Long id;
    private String type;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String pinCode;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
