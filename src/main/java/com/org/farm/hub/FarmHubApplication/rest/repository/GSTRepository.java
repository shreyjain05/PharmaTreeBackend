package com.org.farm.hub.FarmHubApplication.rest.repository;

import com.org.farm.hub.FarmHubApplication.rest.entity.GST;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTRepository extends JpaRepository<GST, Long> {
}
