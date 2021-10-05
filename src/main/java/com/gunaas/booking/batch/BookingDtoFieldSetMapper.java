package com.gunaas.booking.batch;

import com.gunaas.booking.model.Booking;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 * Row mapper for the csv file (used by spring batch)
 */
@Component
public class BookingDtoFieldSetMapper implements FieldSetMapper<Booking> {

    @Override
    public Booking mapFieldSet(FieldSet fieldSet) throws BindException {
        Integer bookingId = Integer.valueOf(fieldSet.readRawString("booking_id"));
        Integer hotelId = Integer.valueOf(fieldSet.readRawString("hotel_id"));
        String customerId = fieldSet.readRawString("customer_id");
        Integer sellingPriceLocalCurrency = Integer.valueOf(fieldSet.readRawString("selling_price_local_currency"));
        String currency = fieldSet.readRawString("currency");
        Float to_usd_exchange_rate = Float.valueOf(fieldSet.readRawString("to_usd_exchange_rate"));
        return new Booking(bookingId, hotelId, customerId, sellingPriceLocalCurrency, currency, to_usd_exchange_rate);
    }
}
