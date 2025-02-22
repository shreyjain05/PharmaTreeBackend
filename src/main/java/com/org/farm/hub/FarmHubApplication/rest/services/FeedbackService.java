package com.org.farm.hub.FarmHubApplication.rest.services;

import com.org.farm.hub.FarmHubApplication.rest.entity.Feedback;
import com.org.farm.hub.FarmHubApplication.rest.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedback(){
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackByID(Long Id){
        return feedbackRepository.findById(Id);
    }

    public Feedback createFeedback(Feedback feedback){
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Feedback feedback){
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long Id){
        feedbackRepository.deleteById(Id);
    }
}
