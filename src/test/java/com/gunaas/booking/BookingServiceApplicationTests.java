package com.gunaas.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunaas.booking.model.CustomerBookingSummary;
import com.gunaas.booking.model.HotelBookingSummary;
import com.gunaas.booking.service.BookingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * Test cases cover all the fundamental requirements and tests by making connection with data base if multi threading and SQL queries work fine.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookingServiceApplicationTests {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Autowired
    BookingService bookingService;

    @Test
    public void jobRunnnerTest() throws Exception {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);
        Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
    }

    private static Map<String, Float> getExchangeMap() {
        Map<String, Float> exchangeMap = new HashMap<>();
        exchangeMap.put("SGD", 2.22F);
        exchangeMap.put("THB", 4.35F);
        return exchangeMap;
    }

    @Test
    public void hotelBookingSummaryTest() throws Exception {
        List<HotelBookingSummary> hotelBookingSummaryList1 = bookingService.getHotelBookingSummary("1000196", getExchangeMap());
        List<HotelBookingSummary> hotelBookingSummaryList2 = bookingService.getHotelBookingSummary("", getExchangeMap());
        List<HotelBookingSummary> hotelBookingSummaryList3 = bookingService.getHotelBookingSummary("1000196", Collections.emptyMap());
        List<HotelBookingSummary> hotelBookingSummaryList4 = bookingService.getHotelBookingSummary(null, getExchangeMap());
        List<HotelBookingSummary> hotelBookingSummaryList5 = bookingService.getHotelBookingSummary("1000196", null);
        Assert.assertEquals(new ObjectMapper().writeValueAsString(hotelBookingSummaryList1), "[{\"numberOfBookings\":10,\"total_price_usd\":8339.0205}]");
        Assert.assertEquals(new ObjectMapper().writeValueAsString(hotelBookingSummaryList2), "[]");
        Assert.assertEquals(new ObjectMapper().writeValueAsString(hotelBookingSummaryList3), "[{\"numberOfBookings\":10,\"total_price_usd\":4538.6396}]");
        Assert.assertEquals(new ObjectMapper().writeValueAsString(hotelBookingSummaryList4), "[]");
        Assert.assertEquals(new ObjectMapper().writeValueAsString(hotelBookingSummaryList5), "[]");
    }

    private List<String> testCustomerIds() {
        List<String> ids = new ArrayList<>(Arrays.asList("5afe319-9016-41b3-a981-3bc6108baa5c",
                "77da4d3f-d89a-4931-bc08-e0c6d3dd1346",
                "01862efe-b685-4316-88e7-bc791f7bd907",
                "c46d2d00-65be-4e56-9861-de45097882d9",
                "91618cb3-0550-42fd-87c6-3f2762beb095",
                "0f81f7e6-b8df-4dcd-9a67-f01367cda6e8",
                "59e62c2f-2a95-4656-ad26-ea4dab3f912c",
                "a5952167-92b7-4608-b4ab-20e2b372f002"));
        return ids;
    }

    @Test
    public void customerBookingSummaryTest() throws Exception {
        List<CustomerBookingSummary> customerBookingSummaryList1 = bookingService.getCustomersBookingSummary(testCustomerIds());
        List<CustomerBookingSummary> customerBookingSummaryList2 = bookingService.getCustomersBookingSummary(Collections.EMPTY_LIST);
        List<CustomerBookingSummary> customerBookingSummaryList3 = bookingService.getCustomersBookingSummary(null);
        Assert.assertEquals(new ObjectMapper().writeValueAsString(customerBookingSummaryList1), "[{\"customerId\":\"91618cb3-0550-42fd-87c6-3f2762beb095\",\"number_of_bookings\":2,\"total_price_usd\":413.667},{\"customerId\":\"77da4d3f-d89a-4931-bc08-e0c6d3dd1346\",\"number_of_bookings\":3,\"total_price_usd\":1060.943},{\"customerId\":\"c46d2d00-65be-4e56-9861-de45097882d9\",\"number_of_bookings\":3,\"total_price_usd\":646.76},{\"customerId\":\"01862efe-b685-4316-88e7-bc791f7bd907\",\"number_of_bookings\":3,\"total_price_usd\":941.74},{\"customerId\":\"a5952167-92b7-4608-b4ab-20e2b372f002\",\"number_of_bookings\":1,\"total_price_usd\":871.2},{\"customerId\":\"0f81f7e6-b8df-4dcd-9a67-f01367cda6e8\",\"number_of_bookings\":2,\"total_price_usd\":761.44},{\"customerId\":\"59e62c2f-2a95-4656-ad26-ea4dab3f912c\",\"number_of_bookings\":1,\"total_price_usd\":559.02}]");
        Assert.assertEquals(new ObjectMapper().writeValueAsString(customerBookingSummaryList2), "[]");
        Assert.assertEquals(new ObjectMapper().writeValueAsString(customerBookingSummaryList3), "[]");
    }
}
