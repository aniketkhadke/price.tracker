package com.assignment.price.tracker.service;

import com.assignment.price.tracker.dto.PriceTrackingRequest;
import com.assignment.price.tracker.dto.PriceTrackingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackerServiceTest {

    private TrackerService trackerService;

    public TrackerServiceTest() {
    }

    @BeforeEach
    void setUp() {
        trackerService = new TrackerService();
    }

    @Test
    void testSchedulePriceCheckPriceLowerThanTriggerShouldReturnImmediateMatch() throws Exception {
        final PriceTrackingRequest trackingRequest = new PriceTrackingRequest("http://toysworld.com/drum",
                80.00, "*/5 * * * *");

        final ResponseEntity<PriceTrackingResponse> response = trackerService.schedulePriceCheck(trackingRequest);

        assertEquals("PriceMatched", response.getBody().getMessage(),"The message does not match");
    }

    @Test
    void testSchedulePriceCheckPriceHigherThanTriggerShouldScheduleCheck() throws Exception {
        final PriceTrackingRequest trackingRequest = new PriceTrackingRequest("http://toysworld.com/snake-n-ladder", 9.8, "0 */1 * * * *");

        final ResponseEntity<PriceTrackingResponse> response = trackerService.schedulePriceCheck(trackingRequest);

        assertEquals("ScheduledAlert", response.getBody().getMessage(),"The message does not match");
    }


    @Test
    void testSchedulePriceCheckProductNotFoundShouldThrowException() {
        final PriceTrackingRequest trackingRequest = new PriceTrackingRequest("http://toysworld.com/dall-set", 9.8, "0 */1 * * * *");

        assertThrows(Exception.class, () ->
                trackerService.schedulePriceCheck(trackingRequest));
    }

    @Test
    void testCheckPricePriceMatchesShouldCancelScheduler() throws Exception {
        // Setup
        final PriceTrackingRequest request = new PriceTrackingRequest("http://toysworld.com/snake-n-ladder", 9.99, "0 */1 * * * *");

        final ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);

        // Get access to private method
        final Method checkPriceMethod = TrackerService.class.getDeclaredMethod("checkPrice",
                PriceTrackingRequest.class, ScheduledFuture.class);
        checkPriceMethod.setAccessible(true);

        // Execute private method
        checkPriceMethod.invoke(trackerService, request, scheduledFuture);

        // Verify scheduler was cancelled
        verify(scheduledFuture, times(1)).cancel(false);

    }

    @Test
    void testCheckPricePriceHigherShouldNotCancelScheduler() throws Exception {
        // Setup
        final PriceTrackingRequest request = new PriceTrackingRequest("http://toysworld.com/drum", 77.98, "0 */1 * * * *");

        final ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);

        // Get access to private method
        final Method checkPriceMethod = TrackerService.class.getDeclaredMethod("checkPrice",
                PriceTrackingRequest.class, ScheduledFuture.class);
        checkPriceMethod.setAccessible(true);
        // Execute private method
        checkPriceMethod.invoke(trackerService, request, scheduledFuture);

        // Verify scheduler was not canceled
        verify(scheduledFuture, never()).cancel(false);
    }
}
