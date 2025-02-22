package com.org.farm.hub.FarmHubApplication.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.org.farm.hub.FarmHubApplication.rest.entity.*;
import com.org.farm.hub.FarmHubApplication.rest.model.CustomerApproval;

import java.util.ArrayList;
import java.util.List;

public class JsonCreator {

    public static void main(String[] args) throws Exception {

        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule to handle LocalDateTime and other Java 8 types
        objectMapper.registerModule(new JavaTimeModule());

        // Disable timestamp feature for date fields
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        //****************Create your objects here******************//

        // Create an instance of Customer
        Customer customer = new Customer();
        List<CustomerAddress> addresses = new ArrayList<>();
        addresses.add(new CustomerAddress());
        customer.setAddresses(addresses);
        //customer.setGstInformation(new GST());
        //customer.setRoyaltyPoints(new RoyaltyPoints());

        // Convert the Person object to a JSON string
        String jsonStringCustomer = objectMapper.writeValueAsString(customer);

        // Create an instance of Product
        Products products = new Products();
        List<ProductInventory> productInventoryList = new ArrayList<>();
        productInventoryList.add(new ProductInventory());
        products.setProductInventoryList(productInventoryList);

        // Convert the Person object to a JSON string
        String jsonStringProduct = objectMapper.writeValueAsString(products);

        // Create an instance of Order
        Orders orders = new Orders();
        List<OrderItems> orderItemsList = new ArrayList<>();
        orderItemsList.add(new OrderItems());
        orders.setOrderItems(orderItemsList);

        // Convert the Person object to a JSON string
        String jsonStringOrder = objectMapper.writeValueAsString(orders);

        // Create an instance of Approval
        CustomerApproval approval = new CustomerApproval();

        // Convert the Person object to a JSON string
        String jsonStringApproval = objectMapper.writeValueAsString(approval);

        // Create an instance of Promotions
        Promotions promotions = new Promotions();
        promotions.setPromotionDetails(new PromotionDetails());

        // Convert the Person object to a JSON string
        String jsonStringPromo = objectMapper.writeValueAsString(promotions);

        // Create an instance of Payments
        Payments payments = new Payments();

        // Convert the Person object to a JSON string
        String jsonStringPayments = objectMapper.writeValueAsString(payments);

        // Print the JSON string
        System.out.println("\nCustomer : " + jsonStringCustomer);
        System.out.println("\nProduct : " + jsonStringProduct);
        System.out.println("\nOrder : " + jsonStringOrder);
        System.out.println("\nApproval : " + jsonStringApproval);
        System.out.println("\nPromo : " + jsonStringPromo);
        System.out.println("\nPayments : " + jsonStringPayments);
    }
}
