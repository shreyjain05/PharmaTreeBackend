package com.org.farm.hub.FarmHubApplication.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name ="gst_information")
@Getter
@Setter
public class GST {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String GSTNumber;
    private String GSTStateCode;
    private String status;
    private boolean isActive=false;
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();

    @OneToOne(mappedBy = "gstInformation")
    private Customer customer;


}
