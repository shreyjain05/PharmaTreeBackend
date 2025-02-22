package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.DTO.ProductsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Products;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductsDTO>> getProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductsDTO> getCustomerById(@PathVariable String id){
        Optional<ProductsDTO> response = productService.getProductsById(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HubResponseEntity> createProducts(@RequestBody Products products){
        return ResponseEntity.status(HttpStatus.OK).body(productService.createProduct(products));
    }

    @PutMapping
    public ResponseEntity<HubResponseEntity> updateProducts(@RequestBody Products products){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(products));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id){
        productService.deleteProduct(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
