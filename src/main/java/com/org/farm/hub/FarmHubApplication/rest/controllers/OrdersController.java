package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.DTO.OrdersDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrdersDTO>> getOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersDTO> getOrdersById(@PathVariable String id){
        Optional<OrdersDTO> response = orderService.getOrdersByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HubResponseEntity> createOrders(@RequestBody Orders Orders){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrders(Orders));

    }

    @PutMapping
    public ResponseEntity<HubResponseEntity> updateOrders(@RequestBody Orders Orders){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrders(Orders));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrdersById(@PathVariable String id){
        orderService.deleteOrders(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
