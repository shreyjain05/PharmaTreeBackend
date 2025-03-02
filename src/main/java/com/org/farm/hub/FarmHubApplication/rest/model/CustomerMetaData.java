package com.org.farm.hub.FarmHubApplication.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerMetaData {

    private String customerDiscount;
    private List<String> wishedProducts = new ArrayList<String>();
    private List<CustomerTarget> customerTargets= new ArrayList<>();
    private boolean isCreditAllowed;
}
