package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.entity.RoyaltyHistory;
import com.org.farm.hub.FarmHubApplication.rest.repository.RoyaltyHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoyaltyHistoryService {

    @Autowired
    RoyaltyHistoryRepository royaltyHistoryRepository;

    public List<RoyaltyHistory> getAllRoyaltyHistory(){
        return royaltyHistoryRepository.findAll();
    }

    public Optional<RoyaltyHistory> getRoyaltyHistoryByID(Long Id){
        return royaltyHistoryRepository.findById(Id);
    }

    public RoyaltyHistory createPayment(RoyaltyHistory Payment){
        return royaltyHistoryRepository.save(Payment);
    }

    public RoyaltyHistory updateRoyaltyHistory(RoyaltyHistory Payment){
        return royaltyHistoryRepository.save(Payment);
    }

    public void deleteRoyaltyHistory(Long Id){
        royaltyHistoryRepository.deleteById(Id);
    }
}
