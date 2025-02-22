package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity(name="payments_update_items")
public class PaymentsItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderID;
    private String invoiceNumber;
    private String amount;
    private Date paymentDate;
    private String customerID;
    private String createdBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentUpdates paymentUpdates;
}
