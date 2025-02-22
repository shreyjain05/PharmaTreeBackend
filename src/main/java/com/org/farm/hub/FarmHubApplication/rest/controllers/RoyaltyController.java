package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.entity.RoyaltyPoints;
import com.org.farm.hub.FarmHubApplication.rest.services.RoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/royalty")
public class RoyaltyController {

    @Autowired
    RoyaltyService royaltyService;

    @GetMapping
    public ResponseEntity<List<RoyaltyPoints>> getRoyaltyPoints(){
        return ResponseEntity.status(HttpStatus.OK).body(royaltyService.getAllRoyaltyPoints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoyaltyPoints> getRoyaltyPointsById(@PathVariable String id){
        Optional<RoyaltyPoints> response = royaltyService.getRoyaltyPointsByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoyaltyPoints> createRoyaltyPoints(@RequestBody RoyaltyPoints royaltyPoints){
        return ResponseEntity.status(HttpStatus.CREATED).body(royaltyService.createPayment(royaltyPoints));

    }

    @PutMapping
    public ResponseEntity<RoyaltyPoints> updateRoyaltyPoints(@RequestBody RoyaltyPoints royaltyPoints){
        return ResponseEntity.status(HttpStatus.OK).body(royaltyService.updateRoyaltyPoints(royaltyPoints));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoyaltyPointsById(@PathVariable String id){
        royaltyService.deleteRoyaltyPoints(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
