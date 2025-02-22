package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.entity.RoyaltyHistory;
import com.org.farm.hub.FarmHubApplication.rest.services.RoyaltyHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/royaltyHistory")
public class RoyaltyHistoryController {

    @Autowired
    RoyaltyHistoryService royaltyHistoryService;

    @GetMapping
    public ResponseEntity<List<RoyaltyHistory>> getRoyaltyHistory(){
        return ResponseEntity.status(HttpStatus.OK).body(royaltyHistoryService.getAllRoyaltyHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoyaltyHistory> getRoyaltyHistoryById(@PathVariable String id){
        Optional<RoyaltyHistory> response = royaltyHistoryService.getRoyaltyHistoryByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoyaltyHistory> createRoyaltyHistory(@RequestBody RoyaltyHistory royaltyHistory){
        return ResponseEntity.status(HttpStatus.CREATED).body(royaltyHistoryService.createPayment(royaltyHistory));

    }

    @PutMapping
    public ResponseEntity<RoyaltyHistory> updateRoyaltyHistory(@RequestBody RoyaltyHistory royaltyHistory){
        return ResponseEntity.status(HttpStatus.OK).body(royaltyHistoryService.updateRoyaltyHistory(royaltyHistory));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoyaltyHistoryById(@PathVariable String id){
        royaltyHistoryService.deleteRoyaltyHistory(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
