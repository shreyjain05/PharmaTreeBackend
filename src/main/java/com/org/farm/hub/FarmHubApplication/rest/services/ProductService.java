package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.ProductsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Products;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public HubResponseEntity createProduct(Products products) {
        HubResponseEntity response = new HubResponseEntity();
        if (products.getProductInventoryList() != null) {
            products.getProductInventoryList().forEach(item -> item.setProduct(products));
        }
        productRepository.save(products);
        response.setMessage("Product Created Successfully");
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
