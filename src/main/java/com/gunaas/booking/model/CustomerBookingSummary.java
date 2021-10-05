package com.gunaas.booking.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Customer Booking Summary that we use it in response.
 */
public class CustomerBookingSummary extends AbstractSummary {

    @JsonProperty
    public String customerId;
    @JsonProperty
    public Integer number_of_bookings;
    @JsonProperty
    public float total_price_usd;

    public CustomerBookingSummary(String customerId) {
        this.customerId = customerId;
        this.number_of_bookings = 0;
        this.total_price_usd = 0.0F;
    }

    public CustomerBookingSummary() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getNumber_of_bookings() {
        return number_of_bookings;
    }

    public void setNumber_of_bookings(Integer number_of_bookings) {
        this.number_of_bookings = number_of_bookings;
    }

    public float getTotal_price_usd() {
        return total_price_usd;
    }

    public void setTotal_price_usd(float total_price_usd) {
        this.total_price_usd = total_price_usd;
    }
}
