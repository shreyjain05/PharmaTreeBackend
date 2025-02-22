package com.org.farm.hub.FarmHubApplication.rest.controllers;

import com.org.farm.hub.FarmHubApplication.rest.entity.Feedback;
import com.org.farm.hub.FarmHubApplication.rest.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<List<Feedback>> getFeedback(){
        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.getAllFeedback());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable String id){
        Optional<Feedback> response = feedbackService.getFeedbackByID(Long.valueOf(id));
        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback){
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createFeedback(feedback));

    }

    @PutMapping
    public ResponseEntity<Feedback> updateFeedback(@RequestBody Feedback feedback){
        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.updateFeedback(feedback));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedbackById(@PathVariable String id){
        feedbackService.deleteFeedback(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Entity Deleted Successfully");
    }

}
