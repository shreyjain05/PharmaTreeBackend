package com.org.farm.hub.FarmHubApplication.rest.entity;

import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name="products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String composition;
    private String packingName;
    private String companyName;
    private String status= String.valueOf(Status.ACTIVE);
    private String image;
    private boolean isActive=true;
    private String discount;
    private String createdBy ="ADMIN";
    private String updatedBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();
    private String productCode;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInventory> productInventoryList;
}
