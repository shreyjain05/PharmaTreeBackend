package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.DTO.PromotionsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Promotions;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.services.PromotionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionsController {

    @Autowired
    PromotionsService promotionsService;

    @GetMapping
    public ResponseEntity<List<PromotionsDTO>> getPromotions(){
        return ResponseEntity.status(HttpStatus.OK).body(promotionsService.getAllPromotions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionsDTO> getPromotionsById(@PathVariable String id){
        Optional<PromotionsDTO> response = promotionsService.getPromotionsByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HubResponseEntity> createPromotions(@RequestBody Promotions promotions){
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionsService.createPromotion(promotions));

    }

    @PutMapping
    public ResponseEntity<HubResponseEntity> updatePromotions(@RequestBody Promotions promotions){
        return ResponseEntity.status(HttpStatus.OK).body(promotionsService.updatePromotions(promotions));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePromotionsById(@PathVariable String id){
        promotionsService.deletePromotions(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
