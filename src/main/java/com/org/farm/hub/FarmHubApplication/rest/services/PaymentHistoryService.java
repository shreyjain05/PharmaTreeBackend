package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.PaymentsUpdateDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.PaymentUpdates;
import com.org.farm.hub.FarmHubApplication.rest.entity.Payments;
import com.org.farm.hub.FarmHubApplication.rest.entity.PaymentsItems;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.PaymentUpdatesRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PaymentHistoryService {


    @Autowired
    PaymentUpdatesRepository paymentUpdatesRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentsService paymentService;

    public List<PaymentsUpdateDTO> getAllPaymentHistory(){
        List<PaymentsUpdateDTO> payments = new ArrayList<>();
        paymentUpdatesRepository.findAll().forEach(payment -> {
            PaymentsUpdateDTO paymentsUpdateDTO = new PaymentsUpdateDTO();
            modelMapper.map(payment, paymentsUpdateDTO);
            payments.add(paymentsUpdateDTO);
        });
        return payments;
    }

    public Optional<PaymentsUpdateDTO> getPaymentHistoryByID(Long Id){
        Optional<PaymentUpdates> payments = paymentUpdatesRepository.findById(Id);
        if(payments.isPresent()){
            PaymentsUpdateDTO paymentsUpdateDTO = new PaymentsUpdateDTO();
            modelMapper.map(payments.get(), paymentsUpdateDTO);
            return Optional.of(paymentsUpdateDTO);
        }
        else{
            return Optional.empty();
        }
    }

    @Transactional
    public HubResponseEntity createPaymentHistory(PaymentUpdates payment){
        HubResponseEntity response = new HubResponseEntity();
        Random random = new Random();
        //Update payment and order table with received information
        String fileID= "PAY:"+ random.nextInt();
        updatePayments(payment.getPaymentsItems(), fileID).forEach(
                payments -> {
                    orderService.updateOrderWithPayment(payments);
                }
        );
        if (payment.getPaymentsItems() != null) {
            payment.getPaymentsItems().forEach(items -> items.setPaymentUpdates(payment));
        }
        payment.setFileId(fileID);
        paymentUpdatesRepository.save(payment);
        response.setMessage("Payments updated Successfully");
        response.setStatus("SUCCESS");
        return response;
    }

    private List<Payments> updatePayments(List<PaymentsItems> paymentsItems, String fileID) {
        List<Payments> payments = new ArrayList<Payments>();
        paymentsItems.forEach(
                item -> {
                    Payments pay = new Payments();
                    modelMapper.map(item, pay);
                    pay.setPaymentUpdateId(fileID);
                    payments.add(pay);
                }
        );
        return paymentService.createPayments(payments);
    }

    @Transactional
    public PaymentUpdates updatePaymentHistory(PaymentUpdates Payment){
        return paymentUpdatesRepository.save(Payment);
    }

    public void deletePaymentHistory(Long Id){
        paymentUpdatesRepository.deleteById(Id);
    }
}
