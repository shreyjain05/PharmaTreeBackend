package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.DTO.PromotionsDTO;
import com.org.farm.hub.FarmHubApplication.rest.entity.Promotions;
import com.org.farm.hub.FarmHubApplication.rest.model.HubResponseEntity;
import com.org.farm.hub.FarmHubApplication.rest.repository.PromotionsRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionsService {

    @Autowired
    PromotionsRepository promotionsRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<PromotionsDTO> getAllPromotions(){
        List<PromotionsDTO> promotions = new ArrayList<>();
        promotionsRepository.findAll().forEach(promotion ->{
            PromotionsDTO promotionsDTO = new PromotionsDTO();
            modelMapper.map(promotion, promotionsDTO);
            promotions.add(promotionsDTO);
        });
        return promotions;
    }

    public Optional<PromotionsDTO> getPromotionsByID(Long Id){
        Optional<Promotions> promotion = promotionsRepository.findById(Id);
        if(promotion.isPresent()){
            PromotionsDTO promotionsDTO = new PromotionsDTO();
            modelMapper.map(promotion.get(), promotionsDTO);
            return Optional.of(promotionsDTO);
        }else {
            return Optional.empty();
        }
    }

    @Transactional
    public HubResponseEntity createPromotion(Promotions promotion){
        HubResponseEntity response = new HubResponseEntity();
        if (promotion.getPromotionDetails() != null) {
            promotion.getPromotionDetails().setPromotions(promotion);
        }
        promotionsRepository.save(promotion);
        response.setMessage("Promotion Created Successfully");
        response.setStatus("SUCCESS");
        return response;
    }



    @Transactional
    public HubResponseEntity updatePromotions(Promotions promotion){
        HubResponseEntity response = new HubResponseEntity();
        promotionsRepository.save(promotion);
        response.setMessage("Customer updated Successfully");
        response.setStatus("SUCCESS");
        return response;
    }

    public void deletePromotions(Long Id){
        promotionsRepository.deleteById(Id);
    }

}
