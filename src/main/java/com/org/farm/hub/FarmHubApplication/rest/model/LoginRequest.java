package com.org.farm.hub.FarmHubApplication.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String mobile;
    private String otp;
    private boolean validate;
}
