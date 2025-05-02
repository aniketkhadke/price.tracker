package com.assignment.price.tracker.controller;

import com.assignment.price.tracker.dto.PriceTrackingRequest;
import com.assignment.price.tracker.dto.PriceTrackingResponse;
import com.assignment.price.tracker.service.TrackerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PriceTrackerController {

    private TrackerService trackerService;

    @PostMapping("/price-tracker")
    public ResponseEntity<PriceTrackingResponse> schedulePriceTracker(@RequestBody @Valid final PriceTrackingRequest trackingRequest) throws Exception {
        return trackerService.schedulePriceCheck(trackingRequest);
    }
}
