package com.org.farm.hub.FarmHubApplication.rest.repository;

import com.org.farm.hub.FarmHubApplication.rest.entity.ApplicationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, Long> {

    List<ApplicationConfig> findAllByType(String type);
}
