package com.gunaas.booking.service.Impl;

import com.gunaas.booking.model.Booking;
import com.gunaas.booking.model.CustomerBookingSummary;
import com.gunaas.booking.model.HotelBookingSummary;
import com.gunaas.booking.model.SummaryView;
import com.gunaas.booking.multithreading.ConnectionPool;
import com.gunaas.booking.service.BookingService;
import com.gunaas.booking.viewer.BookingViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Reading data from MySql using multi threading concept
 * Sping multiple threads to collect different batch of data and finally aggregate and show in the UI
 * (Handled all the corner cases and sycnhronised resource requirements)
 */
@SuppressWarnings("ALL")
@Service
public class BookingServiceImpl implements BookingService {
    private static int querySizeLimit = 100;
    private static int maximumNoOfThreads = 400;
    private static final String RETRIEVE_BOOKINGS_BY_CUSTOMER_SQL = "select * from booking where customer_id in (\"%s\") LIMIT ? OFFSET ?";
    private static final String RETRIEVE_BOOKINGS_BY_HOTEL_SQL = "select * from booking where hotel_id = ? LIMIT ? OFFSET ?";
    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    public ConnectionPool connectionPool;

    @Autowired
    public BookingViewer bookingViewer;

    @Override
    public List<HotelBookingSummary> getHotelBookingSummary(String hotelId, Map<String, Float> currentExchangeRate) {
        ExecutorService executorServiceTwo = Executors.newCachedThreadPool();
        try {
            Long start = System.currentTimeMillis();
            for (int i = 0; i < maximumNoOfThreads; i++) {
                int finalI = i;
                Thread a = new Thread(new Runnable() {
                    Connection connection;

                    @Override
                    public void run() {
                        try {
                            connection = connectionPool.getConnection();
                            PreparedStatement statement = connection.prepareStatement(RETRIEVE_BOOKINGS_BY_HOTEL_SQL);
                            statement.setString(1, hotelId);
                            statement.setInt(2, querySizeLimit);
                            statement.setInt(3, finalI * querySizeLimit);
                            ResultSet rs = statement.executeQuery();
                            int count = 0;
                            while (rs.next()) {
                                count++;
                                Booking booking = new Booking(rs.getInt("booking_id"),
                                        rs.getInt("hotel_id"),
                                        rs.getString("customer_id"),
                                        rs.getInt("selling_price_local_currency"),
                                        rs.getString("currency"),
                                        rs.getFloat("to_usd_exchange_rate"));
                                bookingViewer.addBookingToView(booking, currentExchangeRate, SummaryView.HOTEL);
                            }
                            LOG.info("Thread running of id {} and queried with ----> LIMIT {}, OFFSET {} ---> count {}",
                                    finalI, querySizeLimit, finalI * querySizeLimit, count);
                        } catch (Exception e) {
                            LOG.error("Exception occurred {}", e);
                        } finally {
                            connectionPool.returnConnection(connection);
                            LOG.info(" Thread returned connection of id {}", finalI);
                        }
                    }
                });
                executorServiceTwo.submit(a);
            }
            executorServiceTwo.shutdown();
            executorServiceTwo.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
            LOG.info("Time taken to complete - Hotel Booking View --> " + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (List<HotelBookingSummary>) bookingViewer.getView(SummaryView.HOTEL);
    }

    @Override
    public List<CustomerBookingSummary> getCustomersBookingSummary(List<String> customerIds) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            Long start = System.currentTimeMillis();
            for (int i = 0; i < maximumNoOfThreads; i++) {
                int finalI = i;
                Thread a = new Thread(new Runnable() {
                    Connection connection;

                    @Override
                    public void run() {
                        try {
                            connection = connectionPool.getConnection();
                            String MODIFIED_SQL = String.format(RETRIEVE_BOOKINGS_BY_CUSTOMER_SQL,
                                    customerIds.stream().collect(Collectors.joining("\",\"")));
                            PreparedStatement statement = connection.prepareStatement(MODIFIED_SQL);
                            statement.setInt(1, querySizeLimit);
                            statement.setInt(2, finalI * querySizeLimit);
                            ResultSet rs = statement.executeQuery();
                            int count = 0;
                            while (rs.next()) {
                                count++;
                                Booking booking = new Booking(rs.getInt("booking_id"),
                                        rs.getInt("hotel_id"),
                                        rs.getString("customer_id"),
                                        rs.getInt("selling_price_local_currency"),
                                        rs.getString("currency"),
                                        rs.getFloat("to_usd_exchange_rate"));
                                bookingViewer.addBookingToView(booking, Collections.EMPTY_MAP, SummaryView.CUSTOMER);
                            }
                            LOG.info("Thread running of id {} and queried with ----> LIMIT {}, OFFSET {} ---> count {}",
                                    finalI, querySizeLimit, finalI * querySizeLimit, count);
                        } catch (Exception e) {
                            LOG.error("Exception occurred {}", e);
                        } finally {
                            connectionPool.returnConnection(connection);
                            LOG.info(" Thread returned connection of id {}", finalI);
                        }
                    }
                });
                executorService.submit(a);
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
            LOG.info("Time taken to complete - Customer Booking View --> " + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (List<CustomerBookingSummary>) bookingViewer.getView(SummaryView.CUSTOMER);
    }
}
