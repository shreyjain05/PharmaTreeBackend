package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name="payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderID;
    private String invoiceNumber;
    private String amount;
    private Date paymentDate;
    private Boolean isSynced=Boolean.FALSE;
    private String customerID;
    private String paymentUpdateId;
    private String createdBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();
}
