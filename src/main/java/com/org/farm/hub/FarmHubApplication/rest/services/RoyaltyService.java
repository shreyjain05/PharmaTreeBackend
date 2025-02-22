package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.entity.RoyaltyPoints;
import com.org.farm.hub.FarmHubApplication.rest.repository.RoyaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoyaltyService {
    
    @Autowired
    RoyaltyRepository royaltyRepository;

    public List<RoyaltyPoints> getAllRoyaltyPoints(){
        return royaltyRepository.findAll();
    }

    public Optional<RoyaltyPoints> getRoyaltyPointsByID(Long Id){
        return royaltyRepository.findById(Id);
    }

    public RoyaltyPoints createPayment(RoyaltyPoints Payment){
        return royaltyRepository.save(Payment);
    }

    public RoyaltyPoints updateRoyaltyPoints(RoyaltyPoints Payment){
        return royaltyRepository.save(Payment);
    }

    public void deleteRoyaltyPoints(Long Id){
        royaltyRepository.deleteById(Id);
    }
}
