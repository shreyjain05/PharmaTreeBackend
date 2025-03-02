package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.entity.Customer;
import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    PaymentsService paymentsService;

    @GetMapping
    public ResponseEntity<List<Payments>> getPayments(){
        return ResponseEntity.status(HttpStatus.OK).body(paymentsService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payments> getPaymentsById(@PathVariable String id){
        Optional<Payments> response = paymentsService.getPaymentsByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Payments> createPayments(@RequestBody Payments Payments){
        Payments savedPayment = paymentsService.createPayment(Payments);
        //Long paymentId = savedPayment.getId();
        callERPPaymentsAPI(Payments);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);

    }

    @PutMapping
    public ResponseEntity<Payments> updatePayments(@RequestBody Payments Payments){
        return ResponseEntity.status(HttpStatus.OK).body(paymentsService.updatePayments(Payments));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentsById(@PathVariable String id){
        paymentsService.deletePayments(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }

     private void callERPPaymentsAPI(Payments Payments){
        String apiURL = "http://demo.logicerp.com/api/SaveBankReceipt";

        String username = "LAdmin";
        String password = "1";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("Amount", Payments.getAmount());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);


        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(apiURL, request, String.class);
            System.out.println("ERP API Response is " + response.getBody());
        } catch (Exception e){
            System.err.println("Error calling ERP API " + e.getMessage());
        }


    }
}
