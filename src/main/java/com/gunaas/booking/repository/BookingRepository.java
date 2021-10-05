package com.gunaas.booking.repository;

import com.gunaas.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for JPA
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
