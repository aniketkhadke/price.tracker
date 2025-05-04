package com.assignment.price.tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceTrackingRequest {
    @NotBlank(message = "The product URL is a mandatory parameter.")
    @Pattern(
            regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$",
            message = "The product URL is invalid.")
    private String productUrl;

    @NotNull(message = "The trigger price for alert is a mandatory parameter.")
    @Min(value = 0, message = "The trigger price must be greater than or equal to zero.")
    private Double triggerPrice;

    @NotBlank(message = "The schedule for price checks is a mandatory parameter.")
    private String scheduleInterval;
}
