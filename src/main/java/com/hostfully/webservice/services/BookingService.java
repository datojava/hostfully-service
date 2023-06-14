package com.hostfully.webservice.services;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.HostfullyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final Logger log = LoggerFactory.getLogger(getClass());


    public HostfullyResponse createBooking() throws HostfullyWSException {

        log.info("Creating booking");

        throw new HostfullyWSException(ErrorType.UNKNOWN_ERROR);
        //return new HostfullyResponse();

    }

}
