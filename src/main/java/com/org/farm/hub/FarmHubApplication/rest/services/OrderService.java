package com.org.farm.hub.FarmHubApplication.rest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerAddressDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.CustomerDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.OrderItemsDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.OrdersDTO;
import com.org.farm.hub.FarmHubApplication.rest.constants.Status;
import com.org.farm.hub.FarmHubApplication.rest.entity.OrderItems;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.OrdersRepository;
import com.org.farm.hub.FarmHubApplication.rest.services.CustomerService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    public List<OrdersDTO> getAllOrders(){
        List<OrdersDTO> orders = new ArrayList<>();
        ordersRepository.findAll().forEach(order -> {
            OrdersDTO ordersDTO = new OrdersDTO();
            modelMapper.map(order, ordersDTO);
            orders.add(ordersDTO);
        });
        return orders;
    }

    public Optional<OrdersDTO> getOrdersByID(Long Id){
        Optional<Orders> order = ordersRepository.findById(Id);
        if(order.isPresent()){
            OrdersDTO ordersDTO = new OrdersDTO();
            modelMapper.map(order.get(), ordersDTO);
            return Optional.of(ordersDTO);
        }else {
            return Optional.empty();
        }
    }

    @Transactional
    public HubResponseEntity createOrders(Orders order){
        HubResponseEntity response = new HubResponseEntity();
        //TODO : Create a integration point here with ERP
        //erpClientService.createCustomer();
        
        //TODO : Create a order id and update the product inventory
        Random random = new Random();
        //order.setOrderID(String.valueOf(random.nextInt()));
        order.setOrderID(String.valueOf(Math.abs(new Random().nextInt(1000000))));
        order.setInvoiceNumber("INV:"+order.getOrderID());
        order.setStatus("ORDERED");

        if (order.getCustomerID() == null || order.getCustomerID().trim().isEmpty()) {
            throw new RuntimeException("Customer ID is missing for the order");
        }
        
        Optional<CustomerDTO> customerDetails = getCustomerDetails(order);
        if (customerDetails.isPresent()) {
            order.setCreatedBy(customerDetails.get().getCompleteName());
        } else {
            logger.error("Customer details not found for Order ID: {}", order.getOrderID());
            throw new RuntimeException("Customer Name Not Found");
        }
        //updateInventory(order.getOrderItems());

        // Ensure the relationships are properly set
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }
        Orders createdOrder = ordersRepository.save(order);
        response.setMessage("Order Created Successfully");
        response.setStatus("SUCCESS");
        response.setOrder(modelMapper.map(createdOrder, OrdersDTO.class));

        callERPOrdersAPI(order);

        return response;
    }

    private void updateInventory(List<OrderItems> orderItems) {
        // TODO : Update inventory based on the order items
        orderItems.forEach(item -> inventoryService.updateProductInventory(modelMapper.map(item, OrderItemsDTO.class)));
    }

    @Transactional
    public void updateOrderWithPayment(Payments payment) {
        // TODO : Update order based on payment
        Optional<Orders> order = ordersRepository.findByOrderID(payment.getOrderID());
        if(order.isPresent()) {
            int payedAmount = Integer.parseInt(order.get().getPaidAmount());
            payedAmount= payedAmount + Integer.parseInt(payment.getAmount());
            int balance = Integer.parseInt(order.get().getPendingAmount());
            balance = balance - Integer.parseInt(payment.getAmount());
            order.get().setPaidAmount(String.valueOf(payedAmount));
            order.get().setPendingAmount(String.valueOf(balance));
            order.get().setLastPaymentDate(new Date());
            ordersRepository.save(order.get());
        }
    }

    public void updateOrderWithPayment(List<Payments> paymentsList) {
        // TODO : Update order based on payment
        paymentsList.forEach(this::updateOrderWithPayment);
    }

    @Transactional
    public HubResponseEntity updateOrders(Orders order){
        HubResponseEntity response = new HubResponseEntity();

        Optional<Orders> existingOrderOpt = ordersRepository.findById(order.getId());
        if (!existingOrderOpt.isPresent()) {
            response.setMessage("Order not found");
            response.setStatus("FAILURE");
            return response;
        }

        Orders existingOrder = existingOrderOpt.get();
        existingOrder.setStatus(order.getStatus());
        existingOrder.setModifiedAt(LocalDateTime.now());

        Orders updatedOrder = ordersRepository.save(existingOrder);
        response.setMessage("Order updated Successfully");
        response.setStatus("SUCCESS");
        response.setOrder(modelMapper.map(updatedOrder, OrdersDTO.class));
        return response;
    }

    public void deleteOrders(Long Id){
        ordersRepository.deleteById(Id);
    }

    private Optional<CustomerDTO> getCustomerDetails(Orders order){
    Long customerId = Long.valueOf(order.getCustomerID());
    Optional<CustomerDTO> customer = customerService.getCustomersByID(customerId);
    return customer;
    }

    private void callERPOrdersAPI(Orders order){
        String apiURL = "http://demo.logicerp.com/api/SaveSaleOrder";

        String username = "LAdmin";
        String password = "1";

        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(new Date());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Branch_Code", "2");
        requestBody.put("Order_Prefix", "PT");
        requestBody.put("Order_Date", formattedDate);
        requestBody.put("Party_User_code", order.getCustomerID());
        requestBody.put("Party_Order_No", order.getOrderID());
        requestBody.put("RCU_Mobile_No", getCustomerDetails(order).map(CustomerDTO::getMobNumber).orElseThrow(() -> new RuntimeException("Mobile Number Not Found")));
        requestBody.put("RCU_First_Name", getCustomerDetails(order).map(CustomerDTO::getFirstName).orElseThrow(() -> new RuntimeException("First Name Not Found")));

        List<Map<String,Object>> listItems = new ArrayList<>();
        for(OrderItems item : order.getOrderItems()){
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("LogicUser_Code", item.getProductID());
            itemData.put("Order_Qty", item.getQuantity());
            itemData.put("Rate", item.getBillAmount());
            listItems.add(itemData);
        }

        requestBody.put("ListItems", listItems);
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
            ResponseEntity<String> response = restTemplate.postForEntity(apiURL, request, String.class);
            System.out.println("ERP API Response is " + response.getBody());
        } catch (Exception e){
            System.err.println("Error calling ERP API " + e.getMessage());
        }


    }
}
