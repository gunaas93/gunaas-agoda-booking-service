package com.gunaas.booking.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Hotel Booking Summary that we use it in response.
 */
public class HotelBookingSummary extends AbstractSummary {
    @JsonProperty
    public int numberOfBookings;

    @JsonProperty
    public float total_price_usd;

    public HotelBookingSummary() {
        this.numberOfBookings = 0;
        this.total_price_usd = 0.0F;
    }

    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    public void setNumberOfBookings(int numberOfBookings) {
        this.numberOfBookings = numberOfBookings;
    }

    public float getTotal_price_usd() {
        return total_price_usd;
    }

    public void setTotal_price_usd(float total_price_usd) {
        this.total_price_usd = total_price_usd;
    }
}
