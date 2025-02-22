package com.org.farm.hub.FarmHubApplication.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerApproval {

    private String customerId;
    private Boolean approved;
}
