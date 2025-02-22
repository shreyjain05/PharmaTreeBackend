package com.org.farm.hub.FarmHubApplication.rest.model;

import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.OrdersDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Customer;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HubResponseEntity {

    private String message;
    private String status;
    private String errorCode;
    //De attach this later
    private CustomerDTO customer;
    private OrdersDTO order;
    private List<String> frequentProducts;
}