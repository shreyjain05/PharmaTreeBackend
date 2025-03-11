package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.ProductsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.ProductInventory;
import com.org.farm.hub.FarmHubApplication.rest.entity.Products;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductsRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<ProductsDTO> getProducts() {
        List<ProductsDTO> products = new ArrayList<ProductsDTO>();
        productRepository.findAll().forEach(product -> {
            ProductsDTO productDTO = new ProductsDTO();
            modelMapper.map(product, productDTO);
            products.add(productDTO);
        });
        return products;
    }

    public Optional<ProductsDTO> getProductsById(Long id) {
        Optional<Products> products = productRepository.findById(id);
        if (products.isPresent()) {
            ProductsDTO productDTO = new ProductsDTO();
            modelMapper.map(products.get(), productDTO);
            return Optional.of(productDTO);
        }else {
            return Optional.empty();
        }
    }

    @Transactional
    /*public HubResponseEntity createProduct(Products products) {
        HubResponseEntity response = new HubResponseEntity();
        if (products.getProductInventoryList() != null) {
            products.getProductInventoryList().forEach(item -> item.setProduct(products));
        }
        productRepository.save(products);
        response.setMessage("Product Created Successfully");
        response.setStatus("SUCCESS");
        return response;
    }*/
public HubResponseEntity createProduct(Products newProduct) {
    HubResponseEntity response = new HubResponseEntity();

    // Check if product already exists by name
    Optional<Products> existingProductOpt = productRepository.findByName(newProduct.getName());

    if (existingProductOpt.isPresent()) {
        Products existingProduct = existingProductOpt.get();
        
        for (ProductInventory newInventory : newProduct.getProductInventoryList()) {
            boolean batchExists = false;

            // Check if batch already exists
            for (ProductInventory existingInventory : existingProduct.getProductInventoryList()) {
                if (existingInventory.getBatchNumber().equals(newInventory.getBatchNumber())) {
                    // Update existing batch's inventory count
                    int updatedCount = Integer.parseInt(existingInventory.getInventoryCount()) 
                                     + Integer.parseInt(newInventory.getInventoryCount());
                    existingInventory.setInventoryCount(String.valueOf(updatedCount));
                    existingInventory.setModifiedAt(LocalDateTime.now());
                    batchExists = true;
                    break;
                }
            }

            // If batch doesn't exist, add a new entry
            if (!batchExists) {
                newInventory.setProduct(existingProduct);
                existingProduct.getProductInventoryList().add(newInventory);
            }
        }

        // Save the updated product
        productRepository.save(existingProduct);
        response.setMessage("Product updated with new batch successfully");
    } else {
        // If product does not exist, create new product
        if (newProduct.getProductInventoryList() != null) {
            newProduct.getProductInventoryList().forEach(item -> item.setProduct(newProduct));
        }
        productRepository.save(newProduct);
        response.setMessage("Product Created Successfully");
    }

    response.setStatus("SUCCESS");
    return response;
}

    @Transactional
    public HubResponseEntity updateProduct(Products products) {
        HubResponseEntity response = new HubResponseEntity();
        productRepository.save(products);
        response.setMessage("Product updated Successfully");
        response.setStatus("SUCCESS");
        return response;
    }

    public void deleteProduct(Long Id){
        productRepository.deleteById(Id);
    }

}
