package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.entity.OrderItems;
import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import com.org.farm.hub.FarmHubApplication.rest.repository.CustomerRepository;
import com.org.farm.hub.FarmHubApplication.rest.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {


    @Autowired
    OrdersRepository ordersRepository;

    public List<String> getFrequentProducts(String customerID) {
        // Implement logic to fetch frequent products for a customer from the OrdersRepository
        List<Orders> orders = ordersRepository.findOrdersByCustomerIdAndDate(customerID, LocalDateTime.now().minusDays(7));
        //To get the top 4 products which are unique
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItems::getProductID)
                .distinct()
                .collect(Collectors.toList());
    }
}
