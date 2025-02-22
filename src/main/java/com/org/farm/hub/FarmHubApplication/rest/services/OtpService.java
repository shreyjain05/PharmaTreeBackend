package com.org.farm.hub.FarmHubApplication.rest.services;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    public void validateOTP(String mobile) {
        System.out.println("OTP validated Successfully");
    }

    public void sendOTP(String mobile) {
        System.out.println("OTP Sent Successfully");
    }
}
