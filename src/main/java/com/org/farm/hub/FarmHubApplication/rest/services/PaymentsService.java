package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.repository.PaymentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PaymentsService {

    @Autowired
    PaymentsRepository paymentsRepository;

    @Autowired
    OrderService orderService;

    public List<Payments> getAllPayments(){
        return paymentsRepository.findAll();
    }

    public Optional<Payments> getPaymentsByID(Long Id){
        return paymentsRepository.findById(Id);
    }

    @Transactional
    public Payments createPayment(Payments payment){
        payment.setPaymentID(String.valueOf(Math.abs(new Random().nextInt(1000000))));
        //TODO : Update the order table for this payment
        orderService.updateOrderWithPayment(payment);
        return paymentsRepository.save(payment);
    }

    @Transactional
    public List<Payments> createPayments(List<Payments> payment){
        return paymentsRepository.saveAll(payment);
    }

    @Transactional
    public Payments updatePayments(Payments payment){
        return paymentsRepository.save(payment);
    }

    public void deletePayments(Long Id){
        paymentsRepository.deleteById(Id);
    }

}
