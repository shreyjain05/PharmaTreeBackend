package com.org.farm.hub.FarmHubApplication.rest.services;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.farm.hub.FarmHubApplication.rest.DTO.OrderItemsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.OrderItems;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import com.org.farm.hub.FarmHubApplication.rest.repository.OrdersRepository;

@Service
@EnableScheduling
public class InvoiceService {

    private static final Logger logger  = LoggerFactory.getLogger(InvoiceService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String invoiceERPURL = "https://logicapi.logicerp.in/RohtakDistributor/GetSaleInvoice";

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    InventoryService inventoryService;

    @Scheduled(fixedRate = 300000)
    public void fetchInvoiceDetails(){
        try{
            logger.info("Calling Invoice API " + invoiceERPURL);

            String username = "RkdApi";
            String password = "RkdApi@321";

            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + encodedAuth;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = sdf.format(new Date());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("DateFrom", formattedDate);
            requestBody.put("DateTo", formattedDate);

             ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            logger.info("Formatted Request Body: \n{}", json);
        } catch (Exception e) {
            logger.error("Error converting request body to JSON", e);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(invoiceERPURL, request, String.class);
            logger.info("ERP API Response is " + response.getBody());

            JsonNode responseBody = objectMapper.readTree(response.getBody());
            JsonNode getDataNode = responseBody.get("GetData");

            if(getDataNode != null && getDataNode.isArray()){
                List<String> orderNumbers = getDataNode.findValuesAsText("SO_Party_Order_No");
                List<Orders> orderedOrders = orderRepository.findByStatus("ORDERED");

                for(Orders order : orderedOrders){
                    if(orderNumbers.contains(order.getOrderID())){
                        for(JsonNode invoicNode : getDataNode){
                            if(invoicNode.get("SO_Party_Order_No").asText().equals(order.getOrderID())){
                                String billNo = invoicNode.get("Bill_No").asText();
                                order.setStatus("INVOICED");
                                order.setInvoiceNumber(billNo);
                                orderRepository.save(order);

                                updateInventory(order.getOrderItems());

                                break;
                            }
                        }
                    }
                }
            }

        } catch (Exception e){
            logger.info("Error calling ERP API " + e.getMessage());
        }
        }catch(Exception e){
            logger.error("Error while calling Invoice API " + e.getMessage());
        }
    }

    private void updateInventory(List<OrderItems> orderItems) {
        // TODO : Update inventory based on the order items
        orderItems.forEach(item -> inventoryService.updateProductInventory(modelMapper.map(item, OrderItemsDTO.class)));
    }
    
}
