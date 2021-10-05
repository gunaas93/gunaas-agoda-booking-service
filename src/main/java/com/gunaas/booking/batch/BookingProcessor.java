package com.gunaas.booking.batch;

import com.gunaas.booking.model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Booking records processor, we can do additional validations and computations if required.
 */
@Component
public class BookingProcessor implements ItemProcessor<Booking, Booking> {
    private static final Logger LOG = LoggerFactory.getLogger(BookingProcessor.class);

    @Override
    public Booking process(Booking booking) {
        booking.setCreate_time(new Date());
        LOG.info("Processing booking id {}", booking.getBooking_id());
        return booking;
    }
}
