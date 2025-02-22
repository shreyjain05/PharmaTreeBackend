package com.org.farm.hub.FarmHubApplication.rest.entity;

import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name="inventory_updates")
public class InventoryUpdates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileId;
    private String rowCount;
    private String status=String.valueOf(Status.SUCCESS);
    private String createdBy ="ADMIN";
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime modifiedAt=LocalDateTime.now();


    @OneToMany(mappedBy = "inventoryUpdates", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryItems> inventoryItemsList;
}
