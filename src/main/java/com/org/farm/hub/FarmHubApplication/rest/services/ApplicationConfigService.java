package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.constants.ApplicationConfigValue;
import com.org.farm.hub.FarmHubApplication.rest.entity.ApplicationConfig;
import com.org.farm.hub.FarmHubApplication.rest.repository.ApplicationConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationConfigService {

    @Autowired
    ApplicationConfigRepository applicationConfigRepository;

    public List<ApplicationConfig> getAllConfig(){
        return applicationConfigRepository.findAll();
    }

    public List<ApplicationConfig> getApplicationConfig(){
        return applicationConfigRepository.findAllByType(String.valueOf(ApplicationConfigValue.APPLICATION_LEVEL));
    }

    public List<ApplicationConfig> getCustomerConfig(){
        return applicationConfigRepository.findAllByType(String.valueOf(ApplicationConfigValue.CUSTOMER_LEVEL));
    }

    public Optional<ApplicationConfig> getConfigByID(Long Id){
        return applicationConfigRepository.findById(Id);
    }

    public ApplicationConfig createConfig(ApplicationConfig config){
        return applicationConfigRepository.save(config);
    }

    public ApplicationConfig updateConfig(ApplicationConfig config){
        return applicationConfigRepository.save(config);
    }

    public void deleteConfig(Long Id){
        applicationConfigRepository.deleteById(Id);
    }
}
