package com.hostfully.webservice.models.bookings;

import com.hostfully.webservice.models.HostfullyResponse;

import java.util.List;

public class BookingResponse extends HostfullyResponse {

    private List<BookingInfo> items;

    public List<BookingInfo> getItems() {
        return items;
    }

    public void setItems(List<BookingInfo> items) {
        this.items = items;
    }
}
