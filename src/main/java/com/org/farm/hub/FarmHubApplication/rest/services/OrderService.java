package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.OrderItemsDTO;
import com.org.farm.hub.FarmHubApplication.rest.DTO.OrdersDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.OrderItems;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    InventoryService inventoryService;

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
        order.setOrderID(String.valueOf(random.nextInt()));
        order.setInvoiceNumber("INV:"+order.getOrderID());

        updateInventory(order.getOrderItems());

        // Ensure the relationships are properly set
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }
        Orders createdOrder = ordersRepository.save(order);
        response.setMessage("Order Created Successfully");
        response.setStatus("SUCCESS");
        response.setOrder(modelMapper.map(createdOrder, OrdersDTO.class));
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
        Orders createdOrder = ordersRepository.save(order);
        response.setMessage("Order updated Successfully");
        response.setStatus("SUCCESS");
        response.setOrder(modelMapper.map(createdOrder, OrdersDTO.class));
        return response;
    }

    public void deleteOrders(Long Id){
        ordersRepository.deleteById(Id);
    }
}
