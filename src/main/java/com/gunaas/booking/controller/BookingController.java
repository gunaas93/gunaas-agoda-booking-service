package com.gunaas.booking.controller;

import com.gunaas.booking.exception.BookingException;
import com.gunaas.booking.model.CustomerBookingSummary;
import com.gunaas.booking.model.HotelBookingSummary;
import com.gunaas.booking.request.CustomerIdsRequest;
import com.gunaas.booking.service.BookingService;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Rest controller for booking summaries
 */
@RestController
@RequestMapping("/bookingsummary")
public class BookingController {
    private static final Logger LOG = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    BookingService bookingService;

    @GetMapping(value = "/hotel/{hotel_id}")
    public ResponseEntity<List<HotelBookingSummary>> getBookingSummaryForHotel(@PathVariable("hotel_id") String hotel_id,
                                                                               @RequestBody Map<String, Float> currentExchangeRateRequest) throws BookingException {
        if (StringUtils.isBlank(hotel_id)) {
            throw new BookingException("invalid hotel-id");
        }
        LOG.info("Request received for hotel-id {}", hotel_id);
        List<HotelBookingSummary> results = bookingService.getHotelBookingSummary(hotel_id, currentExchangeRateRequest);
        LOG.info("Response of {} number of bookings", results.size());
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

 @GetMapping(value = "customer")
    public ResponseEntity<List<CustomerBookingSummary>> getBookingSummaryForCustomers(@RequestBody @NotNull CustomerIdsRequest customerIdsRequest) {
        LOG.info("Request received for customerIds {}", customerIdsRequest.getCustomerIds());
        List<CustomerBookingSummary> results = bookingService.getCustomersBookingSummary(customerIdsRequest.getCustomerIds());
        LOG.info("Response of {} number of bookings", results.size());
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
