package com.org.farm.hub.FarmHubApplication.rest.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PaymentsUpdateDTO {

    private Long id;

    private String fileName;
    private String fileId;
    private String rowCount;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<PaymentItemsDTO> paymentItemsList;
}
