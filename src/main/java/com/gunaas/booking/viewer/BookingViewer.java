package com.gunaas.booking.viewer;

import com.gunaas.booking.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Booking viewer that manipulates the view to the business requirements.
 */
@Component
public class BookingViewer {
    private Map<String, CustomerBookingSummary> customerBookingSummaryMap;
    private Map<Integer, HotelBookingSummary> hotelBookingSummaryMap;

    public BookingViewer() {
        this.customerBookingSummaryMap = new ConcurrentHashMap<>();
        this.hotelBookingSummaryMap = new ConcurrentHashMap<>();
    }

    public synchronized void addBookingToView(Booking booking, Map<String, Float> conversionRate, SummaryView summaryView) {
        float exchangeRate = conversionRate.get(booking.getCurrency()) != null
                ? conversionRate.get(booking.getCurrency()) : booking.getTo_usd_exchange_rate();
        switch (summaryView) {
            case HOTEL:
                HotelBookingSummary hotelBookingSummary = hotelBookingSummaryMap.get(booking.getHotel_id()) != null ?
                        hotelBookingSummaryMap.get(booking.getHotel_id()) : new HotelBookingSummary();
                hotelBookingSummary.numberOfBookings++;
                hotelBookingSummary.setTotal_price_usd(hotelBookingSummary.getTotal_price_usd()
                        + (booking.getSelling_price_local_currency() * exchangeRate));
                hotelBookingSummaryMap.put(booking.getHotel_id(), hotelBookingSummary);
                break;
            case CUSTOMER:
                CustomerBookingSummary customerBookingSummary = customerBookingSummaryMap.get(booking.getCustomer_id()) != null ?
                        customerBookingSummaryMap.get(booking.getCustomer_id()) : new CustomerBookingSummary(booking.getCustomer_id());
                customerBookingSummary.setCustomerId(booking.getCustomer_id());
                customerBookingSummary.number_of_bookings++;
                customerBookingSummary.setTotal_price_usd(customerBookingSummary.getTotal_price_usd()
                        + (booking.getSelling_price_local_currency() * exchangeRate));
                customerBookingSummaryMap.put(booking.getCustomer_id(), customerBookingSummary);
                break;
            default:
                break;
        }
    }

    /**
     * Gets the view from the viewer.
     *
     * @param summaryView
     * @return Summaries
     */
    public synchronized List<? extends AbstractSummary> getView(SummaryView summaryView) {
        List<HotelBookingSummary> outputHotelSummaryList = new ArrayList<>(hotelBookingSummaryMap.values());
        List<CustomerBookingSummary> outputCustomerSummaryList = new ArrayList<>(customerBookingSummaryMap.values());
        resetMaps();

        switch (summaryView) {
            case HOTEL:
                return outputHotelSummaryList;
            case CUSTOMER:
                return outputCustomerSummaryList;
            default:
                return null;
        }
    }

    public void resetMaps() {
        customerBookingSummaryMap.clear();
        hotelBookingSummaryMap.clear();
    }
}
