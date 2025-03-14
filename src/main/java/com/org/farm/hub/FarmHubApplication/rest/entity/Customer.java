package com.org.farm.hub.FarmHubApplication.rest.entity;

import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "customer")
@DynamicInsert
@Getter
@Setter
@SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", initialValue = 1297, allocationSize = 1)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    private Long id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAddress> addresses = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gst_id", referencedColumnName = "id")
    private GST gstInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "royalty_id", referencedColumnName = "id")
    private RoyaltyPoints royaltyPoints;

    private String firstName;
    private String lastName;
    private String businessName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private String mobNumber;
    private String pan;
    private String constitution;
    private Date inagurationDate;
    private Date drugExpirationDate20B;
    private String drugLicenseNumber20B;
    private String drugLicenseNumber20BImage;
    private Date drugExpirationDate21B;
    private String drugLicenseNumber21B;
    private String drugLicenseNumber21BImage;
    private Date foodLicenseExpirationDate;
    private String foodLicenseNumber;
    private String shopImage;
    private String isAdmin="CUST";
    @Lob
    @Column(columnDefinition = "json")
    private String metaData;
    private String status= String.valueOf(Status.INACTIVE);
    private boolean isActive=false;
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();
    private String createdBy ="ADMIN";
    private String comments;

}
