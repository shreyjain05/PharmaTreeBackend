package com.org.farm.hub.FarmHubApplication.rest.entity;

import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name="orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderID;
    private String invoiceNumber;
    private String customerID;
    private String totalItems;
    private String billAmount;
    private String totalCGST;
    private String totalSGST;
    private String totalBill;
    private String discount;
    private String paidAmount;
    private String pendingAmount;
    private Date lastPaymentDate;
    private String status;
    private String onBehalf;
    private Boolean isSynced=Boolean.FALSE;
    private String deliveryAddress;
    private String paymentUpdateId;
    private String createdBy;
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();
    private String invoicePDFLink;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems;
}
