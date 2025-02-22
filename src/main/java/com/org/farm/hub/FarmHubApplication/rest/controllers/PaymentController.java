package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentsService.createPayment(Payments));

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
}
