package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name ="application_config")
@Getter
@Setter
public class ApplicationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;
    private String type;
    private String createdBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();
}
