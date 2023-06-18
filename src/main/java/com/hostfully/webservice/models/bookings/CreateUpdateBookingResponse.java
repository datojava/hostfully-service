package com.hostfully.webservice.models.bookings;

import com.hostfully.webservice.models.HostfullyResponse;

public class CreateUpdateBookingResponse extends HostfullyResponse {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
