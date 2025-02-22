package com.org.farm.hub.FarmHubApplication.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerTarget {

    private String targetType;
    private String targetValue;
    private Date targetStartDate;
    private Date targetEndDate;
    private String achievedValue;
}
