package com.org.farm.hub.FarmHubApplication.rest.repository;

import com.org.farm.hub.FarmHubApplication.rest.entity.PaymentUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentUpdatesRepository extends JpaRepository<PaymentUpdates, Long> {
}
