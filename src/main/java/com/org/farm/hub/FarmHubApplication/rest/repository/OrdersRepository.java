package com.org.farm.hub.FarmHubApplication.rest.repository;

import com.org.farm.hub.FarmHubApplication.rest.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderID(String orderID);

    @Query("SELECT o FROM orders o WHERE o.customerID = :customerId AND o.createdAt >= :startDate")
    List<Orders> findOrdersByCustomerIdAndDate(@Param("customerId") String customerId,
                                               @Param("startDate") LocalDateTime startDate);

    List<Orders> findByStatus(String status);
}
