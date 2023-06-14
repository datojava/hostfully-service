package com.hostfully.webservice.controllers;

import com.hostfully.webservice.annotations.Monitor;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BookingService bookingService;

    @Autowired
    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Monitor
    @PostMapping("booking/create")
    public HostfullyResponse create() throws Exception {

        return bookingService.createBooking();


    }

}
