package com.hostfully.webservice.services;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.constants.HostfullyConstants;
import com.hostfully.webservice.entities.Booking;
import com.hostfully.webservice.entities.Guest;
import com.hostfully.webservice.entities.Property;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.bookings.BookingInfo;
import com.hostfully.webservice.models.bookings.BookingResponse;
import com.hostfully.webservice.models.bookings.CreateBookingResponse;
import com.hostfully.webservice.repositories.BlockRepository;
import com.hostfully.webservice.repositories.BookingRepository;
import com.hostfully.webservice.repositories.GuestRepository;
import com.hostfully.webservice.repositories.PropertyRepository;
import com.hostfully.webservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    private final GuestRepository guestRepository;

    private final BlockRepository blockRepository;

    @Autowired
    public BookingService(final BookingRepository bookingRepository,
                          final PropertyRepository propertyRepository,
                          final GuestRepository guestRepository,
                          final BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.guestRepository = guestRepository;
        this.blockRepository = blockRepository;
    }

    public CreateBookingResponse createBooking(BookingInfo bookingInfo) throws HostfullyWSException {

        log.info("Creating booking...");

        validateBooking(bookingInfo);

        Optional<Property> selectedProp = propertyRepository.findById(bookingInfo.getPropertyId());

        if (selectedProp.isEmpty()) {
            throw new HostfullyWSException(ErrorType.PROPERTY_NOT_FOUND, new Object[]{bookingInfo.getPropertyId()});
        }

        Optional<Guest> selectedGuest = guestRepository.findById(bookingInfo.getGuestId());

        if (selectedGuest.isEmpty()) {
            throw new HostfullyWSException(ErrorType.GUEST_NOT_FOUND, new Object[]{bookingInfo.getGuestId()});
        }

        Booking booking = new Booking();

        try {
            booking.setStartDate(CommonUtils.toDate(bookingInfo.getStartDate()));
            booking.setEndDate(CommonUtils.toDate(bookingInfo.getEndDate()));
        } catch (DateTimeParseException e) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        booking.setProperty(selectedProp.get());
        booking.setGuest(selectedGuest.get());

        if (bookingRepository.bookingExistsInTimeRange(booking.getProperty().getId(), booking.getStartDate(), booking.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PROPERTY_ALREADY_BOOKED);
        }

        if (blockRepository.propertyBlockedInTimeRange(booking.getProperty().getId(), booking.getStartDate(), booking.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PROPERTY_BLOCKED);
        }

        log.info("Property Name: {}", booking.getProperty().getName());
        log.info("Guest:         {}", booking.getGuest().getFullName());
        log.info("Start Date:    {}", bookingInfo.getStartDate());
        log.info("End Date:      {}", bookingInfo.getEndDate());

        CreateBookingResponse response = new CreateBookingResponse();

        response.setId(bookingRepository.save(booking).getId());

        log.info("Booking created");

        return response;

    }

    public HostfullyResponse updateBooking(BookingInfo bookingInfo) throws HostfullyWSException {

        log.info("Updating booking");

        validateBooking(bookingInfo);

        if (bookingInfo.getId() == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"id"});
        }

        Optional<Booking> selectedBookingOpt = bookingRepository.findById(bookingInfo.getId());

        if (selectedBookingOpt.isEmpty()) {
            throw new HostfullyWSException(ErrorType.BOOKING_NOT_FOUND, new Object[]{bookingInfo.getId()});
        }

        Optional<Property> selectedProp = propertyRepository.findById(bookingInfo.getPropertyId());

        if (selectedProp.isEmpty()) {
            throw new HostfullyWSException(ErrorType.PROPERTY_NOT_FOUND, new Object[]{bookingInfo.getPropertyId()});
        }

        Optional<Guest> selectedGuest = guestRepository.findById(bookingInfo.getGuestId());

        if (selectedGuest.isEmpty()) {
            throw new HostfullyWSException(ErrorType.GUEST_NOT_FOUND, new Object[]{bookingInfo.getGuestId()});
        }

        Booking selectedBooking = selectedBookingOpt.get();

        try {
            selectedBooking.setStartDate(CommonUtils.toDate(bookingInfo.getStartDate()));
            selectedBooking.setEndDate(CommonUtils.toDate(bookingInfo.getEndDate()));
        } catch (DateTimeParseException e) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        selectedBooking.setGuest(selectedGuest.get());
        selectedBooking.setProperty(selectedProp.get());

        if (bookingRepository.bookingExistsInTimeRange(selectedBooking.getProperty().getId(), selectedBooking.getStartDate(), selectedBooking.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PROPERTY_ALREADY_BOOKED);
        }

        if (blockRepository.propertyBlockedInTimeRange(selectedBooking.getProperty().getId(), selectedBooking.getStartDate(), selectedBooking.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PROPERTY_BLOCKED);
        }

        log.info("Property Name: {}", selectedBooking.getProperty().getName());
        log.info("Guest:         {}", selectedBooking.getGuest().getFullName());
        log.info("Start Date:    {}", bookingInfo.getStartDate());
        log.info("End Date:      {}", bookingInfo.getEndDate());

        bookingRepository.save(selectedBooking);

        return new HostfullyResponse();
    }


    public BookingResponse lookupById(Long bookingId) throws HostfullyWSException {

        BookingResponse response = new BookingResponse();

        response.setItems(new ArrayList<>());

        log.info("Looking up booking by id: {}", bookingId);

        if (bookingId == null) {

            List<Booking> items = bookingRepository.findAll();

            for (Booking booking : items) {

                convertAndAdd(booking, response);
            }

        } else if (bookingId <= 0L) {

            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"bookingId"});

        } else {

            bookingRepository.findById(bookingId).ifPresent(booking -> convertAndAdd(booking, response));

        }

        return response;

    }

    public BookingResponse lookupByRange(String startDateValue, String endDateValue) throws HostfullyWSException {
        if (CommonUtils.isNullOrEmpty(startDateValue)) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"startDate"});
        }

        if (CommonUtils.isNullOrEmpty(endDateValue)) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"endDate"});
        }

        log.info("Looking up booking(s) by time range: {} - {}", startDateValue, endDateValue);

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = CommonUtils.toDate(startDateValue);
            endDate = CommonUtils.toDate(endDateValue);
        } catch (DateTimeParseException e) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        BookingResponse response = new BookingResponse();

        response.setItems(new ArrayList<>());

        for (Booking booking : bookingRepository.findAllByBookingsForRange(startDate, endDate)) {
            convertAndAdd(booking, response);
        }

        return response;
    }

    public HostfullyResponse deleteBooking(Long bookingId) throws Exception {
        if (bookingId == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"id"});
        }

        Optional<Booking> selectedBookingOpt = bookingRepository.findById(bookingId);

        if (selectedBookingOpt.isEmpty()) {
            throw new HostfullyWSException(ErrorType.BOOKING_NOT_FOUND, new Object[]{bookingId});
        }

        log.info("Deleting booking by id: {}", bookingId);

        bookingRepository.delete(selectedBookingOpt.get());

        return new HostfullyResponse();
    }

    private void convertAndAdd(Booking booking, BookingResponse response) {
        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setId(booking.getId());
        bookingInfo.setGuestId(booking.getGuest().getId());
        bookingInfo.setPropertyId(booking.getProperty().getId());
        bookingInfo.setStartDate(booking.getStartDate().format(DateTimeFormatter.ofPattern(HostfullyConstants.DEFAULT_DATE_PATTERN)));
        bookingInfo.setEndDate(booking.getEndDate().format(DateTimeFormatter.ofPattern(HostfullyConstants.DEFAULT_DATE_PATTERN)));

        response.getItems().add(bookingInfo);

    }

    private void validateBooking(BookingInfo bookingInfo) throws HostfullyWSException {
        if (bookingInfo == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"bookingInfo"});
        }

        if (bookingInfo.getPropertyId() == null || bookingInfo.getPropertyId() <= 0L) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"propertyId"});
        }

        if (bookingInfo.getGuestId() == null || bookingInfo.getGuestId() <= 0L) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"guestId"});
        }

        if (CommonUtils.isNullOrEmpty(bookingInfo.getStartDate())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"startDate"});
        }

        if (CommonUtils.isNullOrEmpty(bookingInfo.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"endDate"});
        }
    }


}