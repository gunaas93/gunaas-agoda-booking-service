package com.gunaas.booking.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Booking entity for the table "booking"
 */
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    private Integer booking_id;
    private Integer hotel_id;
    private String customer_id;
    private Integer selling_price_local_currency;
    private String currency;
    private Float to_usd_exchange_rate;
    private Date create_time;


    public Booking(Integer booking_id, Integer hotel_id, String customer_id, Integer selling_price_local_currency, String currency, Float to_usd_exchange_rate) {
        this.booking_id = booking_id;
        this.hotel_id = hotel_id;
        this.customer_id = customer_id;
        this.selling_price_local_currency = selling_price_local_currency;
        this.currency = currency;
        this.to_usd_exchange_rate = to_usd_exchange_rate;
    }

    public Booking() {
    }

    public Integer getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Integer booking_id) {
        this.booking_id = booking_id;
    }

    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getSelling_price_local_currency() {
        return selling_price_local_currency;
    }

    public void setSelling_price_local_currency(Integer selling_price_local_currency) {
        this.selling_price_local_currency = selling_price_local_currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getTo_usd_exchange_rate() {
        return to_usd_exchange_rate;
    }

    public void setTo_usd_exchange_rate(Float to_usd_exchange_rate) {
        this.to_usd_exchange_rate = to_usd_exchange_rate;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "booking_id=" + booking_id +
                ", hotel_id=" + hotel_id +
                ", customer_id=" + customer_id +
                ", selling_price_local_currency=" + selling_price_local_currency +
                ", currency=" + currency +
                ", to_usd_exchange_rate=" + to_usd_exchange_rate +
                ", create_time=" + create_time +
                '}';
    }
}
