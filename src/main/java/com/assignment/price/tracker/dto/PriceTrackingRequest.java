package com.assignment.price.tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
public class PriceTrackingRequest {
    @NotBlank(message = "The product URL is a mandatory parameter.")
    @Pattern(
            regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$",
            message = "The product URL is invalid.")
    private String productUrl;

    @NotNull(message = "The trigger price for alert is a mandatory parameter.")
    private Double triggerPrice;

    @NotBlank(message = "The schedule for price checks is a mandatory parameter.")
    private String scheduleInterval;
}
