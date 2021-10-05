package com.gunaas.booking.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * POJO request for summaries
 */
public class CustomerIdsRequest {
    @JsonProperty
    List<String> customerIds;

    @JsonCreator
    public CustomerIdsRequest(List<String> customerIds) {
        this.customerIds = customerIds;
    }

    public List<String> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<String> customerIds) {
        this.customerIds = customerIds;
    }
}
