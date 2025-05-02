package com.assignment.price.tracker.service;

import com.assignment.price.tracker.dto.PriceTrackingRequest;
import com.assignment.price.tracker.dto.PriceTrackingResponse;
import com.assignment.price.tracker.dto.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@NoArgsConstructor
public class TrackerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerService.class);

    public ResponseEntity<PriceTrackingResponse> schedulePriceCheck(@Valid final PriceTrackingRequest trackingRequest) throws Exception {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.initialize();
        PriceTrackingResponse trackingResponse;
        final Optional<Product> product = getProducts(trackingRequest.getProductUrl());
        if (product.isPresent()) {
            if (product.get().getPrice().compareTo(trackingRequest.getTriggerPrice()) <= 0) {
                if(log.isInfoEnabled()) {
                    LOGGER.info(String.format("The current price for %s is %.2f and matching user's quote. Skipping scheduler.",
                            trackingRequest.getProductUrl(),
                            product.get().getPrice()));
                }
                trackingResponse = buildPriceTrackingResponse(trackingRequest, product, "PriceMatched");
            } else {
                if(log.isInfoEnabled()) {
                    LOGGER.info(String.format("Product price is not in range, scheduling a task for checking price of %s.",
                            trackingRequest.getProductUrl()));
                }
                final ScheduledFuture<?>[] schedule = new ScheduledFuture[1];
                final Runnable task = () -> {
                    checkPrice(trackingRequest, schedule[0]);
                };
                schedule[0] = scheduler.schedule(task, new CronTrigger(trackingRequest.getScheduleInterval()));
                trackingResponse = buildPriceTrackingResponse(trackingRequest, product, "ScheduledAlert");
            }
        } else {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("The requested product is not available.");
            }
            throw new Exception("The requested product is not available.");
        }

        return ResponseEntity.ok(trackingResponse);
    }

    private static PriceTrackingResponse buildPriceTrackingResponse(final PriceTrackingRequest trackingRequest, final Optional<Product> product, final String message) {
        PriceTrackingResponse trackingResponse;
        trackingResponse = PriceTrackingResponse.builder().currentPrice(product.get().getPrice())
                .triggerPrice(trackingRequest.getTriggerPrice())
                .message(message)
                .productUrl(trackingRequest.getProductUrl())
                .timestamp(Timestamp.from(Instant.now())).build();
        return trackingResponse;
    }

    private void checkPrice(final PriceTrackingRequest trackingRequest, final ScheduledFuture<?> schedule) {
        try {
            final Optional<Product> product = this.getProducts(trackingRequest.getProductUrl());
            if (product.isPresent()) {
                if (product.get().getPrice().compareTo(trackingRequest.getTriggerPrice()) <= 0) {
                    LOGGER.info("Product price matches, cancelling scheduler");
                    //TODO: A notification can be triggered either through - Email, SMS etc.
                    schedule.cancel(false);
                } else {
                    LOGGER.info("Product price still out of reach");
                }
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Optional<Product> getProducts(final String productUrl) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream inputStream = new FileInputStream("products-data.json");
        final List<Product> products = mapper.readValue(inputStream, new TypeReference<>() {
        });
        return products.stream()
                .filter(p -> p.getProductUrl().equalsIgnoreCase(productUrl)).findFirst();
    }
}