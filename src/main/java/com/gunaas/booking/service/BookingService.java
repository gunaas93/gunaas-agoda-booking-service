package com.gunaas.booking.service;

import com.gunaas.booking.model.CustomerBookingSummary;
import com.gunaas.booking.model.HotelBookingSummary;

import java.util.List;
import java.util.Map;

/**
 * Booking service
 */
public interface BookingService {

    public List<HotelBookingSummary> getHotelBookingSummary(String hotelId, Map<String, Float> currentExchangeRate);

    public List<CustomerBookingSummary> getCustomersBookingSummary(List<String> customerIds);
}
