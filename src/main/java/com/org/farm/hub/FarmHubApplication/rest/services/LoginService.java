package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Customer;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.model.LoginRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    OtpService otpService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ApplicationService applicationService;

    public HubResponseEntity doLogin(LoginRequest request) {
        HubResponseEntity response = new HubResponseEntity();
            if(Objects.nonNull(request) && request.getMobile() != null && !request.getMobile().isEmpty()){
                Optional<Customer> customer = customerService.getCustomer(request.getMobile());
                if(customer.isPresent()){
                    if(request.isValidate())
                    {
                        response.setFrequentProducts(applicationService.getFrequentProducts(String.valueOf(customer.get().getId())));
                        otpService.validateOTP(request.getMobile());
                        response.setStatus("SUCCESS");
                        response.setMessage("Login Success");
                        response.setCustomer( modelMapper.map(customer.get(), CustomerDTO.class));
                    }else {
                        otpService.sendOTP(request.getMobile());
                        response.setStatus("IN_PROGRESS");
                        response.setMessage("Login Success");
                    }
                }else{
                    response.setMessage("No Customer registered with this number");
                    response.setStatus("FAILED");
                    response.setErrorCode("FH-002");
                }

            }else {
                response.setMessage("Missing Required Field");
                response.setStatus("FAILED");
                response.setErrorCode("FH-001");
            }
            return response;
    }
}
