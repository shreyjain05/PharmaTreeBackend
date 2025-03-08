package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CustomerDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String businessName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private String mobNumber;
    private String pan;
    private String constitution;
    private Date inagurationDate;
    private Date drugExpirationDate20B;
    private String drugLicenseNumber20B;
    private String drugLicenseNumber20BImage;
    private Date drugExpirationDate21B;
    private String drugLicenseNumber21B;
    private String drugLicenseNumber21BImage;
    private Date foodLicenseExpirationDate;
    private String foodLicenseNumber;
    private String status;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String metaData;
    private String shopImage;
    private boolean isAdmin;
    private String comments;

    private List<CustomerAddressDTO> addresses;

    private GstDTO gstInformation;

    private RoyaltyDTO royaltyPoints;

    public String getCompleteName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}
