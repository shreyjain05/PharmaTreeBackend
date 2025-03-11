package com.org.farm.hub.FarmHubApplication.rest.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import com.org.farm.hub.FarmHubApplication.rest.entity.Customer;
import com.org.farm.hub.FarmHubApplication.rest.entity.CustomerAddress;
import com.org.farm.hub.FarmHubApplication.rest.model.CustomerApproval;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.services.CustomerService;
import com.org.farm.hub.FarmHubApplication.rest.services.OrderService;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

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
        response.setMessage("Customer Successfully!!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    private void callERPCustomerAPI(Customer customer){
        String apiURL = "https://logicapi.logicerp.in/RohtakDistributor/SaveParty";

        String username = "RkdApi";
        String password = "RkdApi@321";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Account_Name", customer.getBusinessName());
        requestBody.put("Account_Type", "CU");
        requestBody.put("User_Code", customer.getId());
        requestBody.put("AddlAccountUserCode", customer.getId());

        CustomerAddress address = customer.getAddresses().get(0);
        Map<String, Object> custDetails = new HashMap<>();
        custDetails.put("First_Add1", address.getAddressLine1());
        custDetails.put("First_Add2", address.getAddressLine2());
        custDetails.put("First_Pincode", address.getPinCode());
        custDetails.put("First_CityName", address.getCity());
        custDetails.put("First_Location",address.getState());
        custDetails.put("Mobile_No", customer.getMobNumber());
        custDetails.put("Email_Address", customer.getEmail());

        requestBody.put("Cust_Details", custDetails);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            logger.info("Formatted Request Body: \n{}", json);
        } catch (Exception e) {
            logger.error("Error converting request body to JSON", e);
        }

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
