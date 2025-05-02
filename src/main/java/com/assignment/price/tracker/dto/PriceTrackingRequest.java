package com.assignment.price.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceTrackingRequest {
    @NotBlank(message = "Product URL is a mandatory parameter.")
    //TODO: Validate URL
    private String productUrl;

    @NotNull(message = "The trigger price for alert is a mandatory parameter.")
    private Double triggerPrice;

    @NotBlank(message = "Schedule for price checks is a mandatory parameter.")
    private String scheduleInterval;
}
