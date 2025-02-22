package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.DTO.PaymentsUpdateDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.PaymentUpdates;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.services.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payment/history")
public class PaymentHistoryController {

    @Autowired
    PaymentHistoryService paymentHistoryService;

    @GetMapping
    public ResponseEntity<List<PaymentsUpdateDTO>> getPaymentUpdates(){
        return ResponseEntity.status(HttpStatus.OK).body(paymentHistoryService.getAllPaymentHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentsUpdateDTO> getPaymentUpdatesById(@PathVariable String id){
        Optional<PaymentsUpdateDTO> response = paymentHistoryService.getPaymentHistoryByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HubResponseEntity> createPaymentUpdates(@RequestBody PaymentUpdates PaymentUpdates){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentHistoryService.createPaymentHistory(PaymentUpdates));

    }

    @PutMapping
    public ResponseEntity<PaymentUpdates> updatePaymentUpdates(@RequestBody PaymentUpdates PaymentUpdates){
        return ResponseEntity.status(HttpStatus.OK).body(paymentHistoryService.updatePaymentHistory(PaymentUpdates));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentUpdatesById(@PathVariable String id){
        paymentHistoryService.deletePaymentHistory(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
