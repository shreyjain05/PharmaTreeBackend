package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PaymentItemsDTO {

    private Long id;
    private String orderID;
    private String invoiceNumber;
    private String amount;
    private Date paymentDate;
    private String customerID;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String discount;
}
