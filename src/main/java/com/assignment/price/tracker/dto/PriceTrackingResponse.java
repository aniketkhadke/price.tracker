package com.assignment.price.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Data
public class PriceTrackingResponse {
    private String message;
    private String productUrl;
    private Double triggerPrice;
    private Double currentPrice;
    private Timestamp timestamp;
}
