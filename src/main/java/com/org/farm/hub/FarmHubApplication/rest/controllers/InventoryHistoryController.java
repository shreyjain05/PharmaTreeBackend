package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.DTO.InventoryUpdatesDTO;
import com.org.farm.hub.FarmHubApplication.rest.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryHistoryController {

    @Autowired
    InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryUpdatesDTO>> getInventoryUpdates(){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.getAllInventoryUpdates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryUpdatesDTO> getInventoryUpdatesById(@PathVariable String id){
        Optional<InventoryUpdatesDTO> response = inventoryService.getInventoryUpdatesByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> updateInventory(@RequestBody InventoryUpdatesDTO updateInventoryRequest){
        inventoryService.createInventoryUpdates(updateInventoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Inventory Updated Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventoryUpdatesById(@PathVariable String id){
        inventoryService.deleteInventoryUpdates(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
