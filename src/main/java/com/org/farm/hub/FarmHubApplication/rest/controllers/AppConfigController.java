package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.entity.ApplicationConfig;
import com.org.farm.hub.FarmHubApplication.rest.services.ApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/applicationConfig")
public class AppConfigController {

    @Autowired
    ApplicationConfigService applicationConfigService;

    @GetMapping
    public ResponseEntity<List<ApplicationConfig>> getAllApplicationConfig(){
        return ResponseEntity.status(HttpStatus.OK).body(applicationConfigService.getAllConfig());
    }

    @GetMapping("/customer")
    public ResponseEntity<List<ApplicationConfig>> getCustomerConfig(){
        return ResponseEntity.status(HttpStatus.OK).body(applicationConfigService.getCustomerConfig());
    }

    @GetMapping("/application")
    public ResponseEntity<List<ApplicationConfig>> getApplicationConfig(){
        return ResponseEntity.status(HttpStatus.OK).body(applicationConfigService.getApplicationConfig());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationConfig> getApplicationConfigById(@PathVariable String id){
        Optional<ApplicationConfig> response = applicationConfigService.getConfigByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApplicationConfig> createApplicationConfig(@RequestBody ApplicationConfig ApplicationConfig){
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationConfigService.createConfig(ApplicationConfig));

    }

    @PutMapping
    public ResponseEntity<ApplicationConfig> updateApplicationConfig(@RequestBody ApplicationConfig ApplicationConfig){
        return ResponseEntity.status(HttpStatus.OK).body(applicationConfigService.updateConfig(ApplicationConfig));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplicationConfigById(@PathVariable String id){
        applicationConfigService.deleteConfig(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }
}
