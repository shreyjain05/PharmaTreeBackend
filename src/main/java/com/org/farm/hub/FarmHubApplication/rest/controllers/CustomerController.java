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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            if(approval.getApproved()){
                customer.setActive(Boolean.TRUE);
                customer.setStatus(String.valueOf(Status.ACTIVE));
            }else{
               customer.setActive(Boolean.FALSE);
               customer.setStatus(String.valueOf(Status.INACTIVE));
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

}
