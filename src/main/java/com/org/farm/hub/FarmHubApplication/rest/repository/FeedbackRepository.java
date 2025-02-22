package com.org.farm.hub.FarmHubApplication.rest.repository;

import com.org.farm.hub.FarmHubApplication.rest.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
