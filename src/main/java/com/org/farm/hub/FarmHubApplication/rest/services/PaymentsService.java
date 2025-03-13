package com.org.farm.hub.FarmHubApplication.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.OrderItems;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.repository.PaymentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentsService {

    @Autowired
    PaymentsRepository paymentsRepository;

    @Autowired
    OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentsService.class);

    public List<Payments> getAllPayments(){
        return paymentsRepository.findAll();
    }

    public Optional<Payments> getPaymentsByID(Long Id){
        return paymentsRepository.findById(Id);
    }

    @Transactional
    public Payments createPayment(Payments payment){
        payment.setPaymentID(String.valueOf(Math.abs(new Random().nextInt(1000000))));
        //TODO : Update the order table for this payment
        callERPBankReceiptsAPI(payment);
        orderService.updateOrderWithPayment(payment);
        return paymentsRepository.save(payment);
    }

    @Transactional
    public List<Payments> createPayments(List<Payments> payment){
        return paymentsRepository.saveAll(payment);
    }

    @Transactional
    public Payments updatePayments(Payments payment){
        return paymentsRepository.save(payment);
    }

    public void deletePayments(Long Id){
        paymentsRepository.deleteById(Id);
    }
    private void callERPBankReceiptsAPI(Payments payments){
        String apiURL = "https://logicapi.logicerp.in/RohtakDistributor/SaveBankReceipt";

        String username = "RkdApi";
        String password = "RkdApi@321";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(new Date());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Branch_Code", "0");
        requestBody.put("ActUserCode", payments.getCustomerID());
        requestBody.put("Remarks_1", payments.getOrderID());
        requestBody.put("Remarks_2", payments.getInvoiceNumber());
        requestBody.put("VoucherDate", formattedDate);

        List<Map<String,Object>> listAccounts = new ArrayList<>();
        Map<String, Object> account = new HashMap<>();
        account.put("Amount", payments.getAmount());
        listAccounts.add(account);
        //account.put("AccountCode", payments.getCustomerID());

        requestBody.put("ListAccounts", listAccounts);
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
            logger.info("ERP API Response is " + response.getBody());
        } catch (Exception e){
            logger.info("Error calling ERP API " + e.getMessage());
        }


    }

}
