package com.org.farm.hub.FarmHubApplication.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.farm.hub.FarmHubApplication.erp.ErpClientService;
import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Customer;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ErpClientService erpClientService;

    @Autowired
    private ModelMapper modelMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<CustomerDTO> getCustomers() {
        List<CustomerDTO> customers = new ArrayList<>();
        customerRepository.findAll().forEach(fetchCustomer -> {
            CustomerDTO customer = new CustomerDTO();
            modelMapper.map(fetchCustomer,customer);
            customers.add(customer);
        });
        return customers;
    }

    public Optional<CustomerDTO> getCustomersByID(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isPresent()){
            CustomerDTO customer = new CustomerDTO();
            modelMapper.map(customerOptional.get(),customer);
            return Optional.of(customer);
        }else {
            return Optional.empty();
        }
    }

    public Optional<Customer> getCustomersEntityByID(Long id) {
        return customerRepository.findById(id);
    }


    @Transactional
    public HubResponseEntity createCustomer(Customer customer) throws JsonProcessingException {
        HubResponseEntity response = new HubResponseEntity();
        if(validateRequest(customer, response)){
            //TODO : Create a integration point here with ERP
            //erpClientService.createCustomer();

            // Ensure the relationships are properly set
            if (customer.getGstInformation() != null) {
                customer.getGstInformation().setCustomer(customer);
            }
            if (customer.getRoyaltyPoints() != null) {
                customer.getRoyaltyPoints().setCustomer(customer);
            }
            if (customer.getAddresses() != null) {
                customer.getAddresses().forEach(address -> address.setCustomer(customer));
            }

            customerRepository.save(customer);
            response.setMessage("Customer Created Successfully");
            response.setStatus("SUCCESS");
        }
        return response;
    }

    @Transactional
    public HubResponseEntity updateCustomer(Customer customer) throws JsonProcessingException {
        HubResponseEntity response = new HubResponseEntity();
        Optional<Customer> customerAvailable = customerRepository.findById(customer.getId());
        if(customerAvailable.isPresent()) {
            // Ensure the relationships are properly set
            if (customer.getGstInformation() != null) {
                customer.getGstInformation().setCustomer(customer);
            }
            if (customer.getRoyaltyPoints() != null) {
                customer.getRoyaltyPoints().setCustomer(customer);
            }
            if (customer.getAddresses() != null) {
                customer.getAddresses().forEach(address -> address.setCustomer(customer));
            }

            customerRepository.save(customer);
            response.setMessage("Customer updated successfully");
            response.setStatus("SUCCESS");
        }else{
            response.setMessage("Customer does not exists with the given id");
            response.setStatus("FAILURE");
        }

        return response;
    }

    private boolean validateRequest(Customer registration, HubResponseEntity response) {

        Optional<Customer> customer = customerRepository.findByMobNumber(registration.getMobNumber());
        if(customer.isPresent()) {
            response.setMessage("Customer already exists with the given mobile number");
            response.setStatus("FAILURE");
            return false;
        }else {
            return true;
        }
        /*if(Objects.isNull(registration)){
            return false;
        }
        return !StringHelper.isEmptyString(registration.getFirstName())
                && !StringHelper.isEmptyString(registration.getLastName())
                && !StringHelper.isEmptyString(registration.getDrugLicenseNumber())
                && !StringHelper.isEmptyString(registration.getMob());*/
    }

    public boolean isCustomerPresent(String mobile){
        Optional<Customer> customer = customerRepository.findByMobNumber(mobile);
        return customer.isPresent();
    }

    public Optional<Customer> getCustomer(String mobile){
        return customerRepository.findByMobNumber(mobile);
    }


}
