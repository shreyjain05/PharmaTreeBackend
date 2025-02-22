package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrdersDTO {

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
    private Boolean isSynced;
    private String deliveryAddress;
    private String paymentUpdateId;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<OrderItemsDTO> orderItems;
}
