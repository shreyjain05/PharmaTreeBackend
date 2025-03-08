package com.org.farm.hub.FarmHubApplication.rest.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import com.org.farm.hub.FarmHubApplication.rest.entity.Customer;
import com.org.farm.hub.FarmHubApplication.rest.model.CustomerApproval;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomer(){
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id){
        Optional<CustomerDTO> response = customerService.getCustomersByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HubResponseEntity> createCustomer(@RequestBody Customer registration) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(registration));

    }

    @PutMapping
    public ResponseEntity<HubResponseEntity> updateCustomer(@RequestBody Customer registration) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(registration));

    }

    @PostMapping("/approval")
    public ResponseEntity<HubResponseEntity> approveCustomer(@RequestBody CustomerApproval approval){
        HubResponseEntity response = new HubResponseEntity();
        Optional<Customer> customers = customerService.getCustomersEntityByID(Long.valueOf(approval.getCustomerId()));
        customers.ifPresent(customer -> {
            if("approved".equalsIgnoreCase(approval.getApproved())){
                customer.setActive(Boolean.TRUE);
                customer.setStatus(String.valueOf(Status.ACTIVE));
                customer.setComments(approval.getComments());
                callERPCustomerAPI(customer);
            }
            else if ("review".equalsIgnoreCase(approval.getApproved())){
                customer.setActive(Boolean.FALSE);
                customer.setStatus(String.valueOf(Status.INACTIVE));
                customer.setComments(approval.getComments());
            }
            else{
               customer.setActive(Boolean.FALSE);
               customer.setStatus(String.valueOf(Status.INACTIVE));
               customer.setComments(approval.getComments());
            }
            try {
                customerService.updateCustomer(customer);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        response.setMessage("Customer Updated Successfully!!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    private void callERPCustomerAPI(Customer customer){
        String apiURL = "http://demo.logicerp.com/api/SaveParty";

        String username = "LAdmin";
        String password = "1";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Account_Name", customer.getBusinessName());
        requestBody.put("Account_Type", "CU");
        requestBody.put("User_Code", customer.getId());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(apiURL, request, String.class);
            System.out.println("ERP API Response is " + response.getBody());
        } catch (Exception e){
            System.err.println("Error calling ERP API " + e.getMessage());
        }


    }

}
