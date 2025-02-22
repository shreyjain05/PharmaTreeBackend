package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.InventoryItemsDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.InventoryUpdatesDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.OrderItemsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.*;
import com.org.farm.hub.FarmHubApplication.rest.repository.InventoryUpdatesRepository;
import com.org.farm.hub.FarmHubApplication.rest.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryService {

    @Autowired
    InventoryUpdatesRepository inventoryUpdatesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ProductsRepository productRepository;

    public List<InventoryUpdatesDTO> getAllInventoryUpdates(){
        List<InventoryUpdatesDTO> inventoryUpdates = new ArrayList<>();
        inventoryUpdatesRepository.findAll().forEach(inventory -> {
            InventoryUpdatesDTO inventoryUpdatesDTO = new InventoryUpdatesDTO();
            modelMapper.map(inventory, inventoryUpdatesDTO);
            inventoryUpdates.add(inventoryUpdatesDTO);
        });
        return inventoryUpdates;
    }

    public Optional<InventoryUpdatesDTO> getInventoryUpdatesByID(Long id){
        Optional<InventoryUpdates> inventoryUpdates =inventoryUpdatesRepository.findById(id);
        if(inventoryUpdates.isPresent()){
            InventoryUpdatesDTO inventory = new InventoryUpdatesDTO();
            modelMapper.map(inventoryUpdates.get(),inventory);
            return Optional.of(inventory);
        }else {
            return Optional.empty();
        }
    }

    @Transactional
    public void createInventoryUpdates(InventoryUpdatesDTO updateInventoryRequest){
        InventoryUpdates inventoryUpdates = new InventoryUpdates();
        inventoryUpdates.setFileName(updateInventoryRequest.getFileName());
        inventoryUpdates.setRowCount(updateInventoryRequest.getRowCount());
        Random random = new Random();
        String fileID= "INV:"+ random.nextInt();
        productRepository.saveAll(updateInventory(updateInventoryRequest.getInventoryItemsList(), productRepository.findAll(), fileID));
        inventoryUpdates.setFileId(fileID);
        inventoryUpdatesRepository.save(inventoryUpdates);
    }

    @Transactional
    public InventoryUpdates updateInventoryUpdates(InventoryUpdates updateInventoryRequest){
        return inventoryUpdatesRepository.save(updateInventoryRequest);
    }

    public void deleteInventoryUpdates(Long Id){
        inventoryUpdatesRepository.deleteById(Id);
    }

    @Transactional
    public void updateProductInventory(OrderItemsDTO itemsDTO){
        Optional<Products> products = productRepository.findById(Long.valueOf(itemsDTO.getProductID()));
        products.ifPresent(product -> {
            // Update product inventory count
            product.getProductInventoryList().forEach(items -> {
                if(items.getBatchNumber().equals(itemsDTO.getBatch())){
                    items.setInventoryCount(itemsDTO.getQuantity());
                }
            });
            productRepository.save(products.get());
        });
    }

    private List<Products> updateInventory(List<InventoryItemsDTO> inventoryProducts, List<Products> productsList, String fileID) {

        // Update product based on the matching id from inventory
        return productsList.stream()
                .peek(products -> {
                    // Find a matching inventoryItems object from listB
                    Optional<InventoryItemsDTO> match = inventoryProducts.stream()
                            .filter(inventoryItems -> Objects.equals(inventoryItems.getProductId(), String.valueOf(products.getId())))
                            .findFirst();

                    // If a match is found
                    match.ifPresent(inventoryItems -> {
                        products.getProductInventoryList().forEach(product -> {
                            if (product.getBatchNumber().equals(inventoryItems.getBatchNumber())) {
                                product.setInventoryCount(inventoryItems.getInventoryCount());
                                product.setInventoryUpdateId(fileID);
                            } else {
                                // If no match found, create a new product
                                ProductInventory productInventory = new ProductInventory();
                                productInventory.setBatchNumber(inventoryItems.getBatchNumber());
                                productInventory.setInventoryCount(inventoryItems.getInventoryCount());
                                productInventory.setBasicRate(inventoryItems.getBasicRate());
                                productInventory.setExpiryDate(inventoryItems.getExpiryDate());
                                productInventory.setMrp(inventoryItems.getMrp());
                                productInventory.setPurchaseRate(inventoryItems.getPurchaseRate());
                                productInventory.setSaleRate(inventoryItems.getSaleRate());
                                productInventory.setDiscount(inventoryItems.getDiscount());
                                productInventory.setInventoryUpdateId(fileID);
                                products.getProductInventoryList().add(productInventory);
                            }
                        });
                    });

                })
                .toList();
    }

}
