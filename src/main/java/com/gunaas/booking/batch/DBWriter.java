package com.gunaas.booking.batch;

import com.gunaas.booking.model.Booking;
import com.gunaas.booking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPS repository to save (upsert into the table booking)
 * using ORM to showcase.
 */
@Component
public class DBWriter implements ItemWriter<Booking> {

    private static final Logger LOG = LoggerFactory.getLogger(DBWriter.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void write(List<? extends Booking> bookings) throws Exception {
        LOG.info("Data Saved for Bookings: " + bookings);
        bookingRepository.saveAll(bookings);
    }
}
