package com.hostfully.webservice.controllers;

import com.hostfully.webservice.annotations.Monitor;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.bookings.BookingInfo;
import com.hostfully.webservice.models.bookings.BookingResponse;
import com.hostfully.webservice.models.bookings.CreateBookingResponse;
import com.hostfully.webservice.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Monitor
    @PostMapping("booking/create")
    public CreateBookingResponse create(@RequestBody BookingInfo bookingInfo) throws Exception {

        return bookingService.createBooking(bookingInfo);

    }

    @Monitor
    @GetMapping("booking/lookup-by-id")
    public BookingResponse lookupById(@RequestParam Long bookingId) throws Exception {

        return bookingService.lookupById(bookingId);

    }

    @Monitor
    @GetMapping("booking/lookup-by-range")
    public BookingResponse lookupByRange(@RequestParam String startDate, @RequestParam String endDate) throws Exception {

        return bookingService.lookupByRange(startDate, endDate);

    }

    @Monitor
    @PutMapping("booking/update")
    public HostfullyResponse update(@RequestBody BookingInfo bookingInfo) throws Exception {

        return bookingService.updateBooking(bookingInfo);

    }

    @Monitor
    @DeleteMapping("booking/delete/{id}")
    public HostfullyResponse delete(@PathVariable Long id) throws Exception {

        return bookingService.deleteBooking(id);

    }

}
