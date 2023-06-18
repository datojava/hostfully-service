package com.hostfully.webservice.repositories;

import com.hostfully.webservice.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.startDate<=:bookingStartDate AND b.endDate<=:bookingEndDate")
    List<Booking> findAllByBookingsForRange(LocalDate bookingStartDate, LocalDate bookingEndDate);

    @Query("SELECT count(b.id)>0 FROM Booking b WHERE b.property.id=:propertyId AND b.startDate<=:bookingStartDate AND b.endDate>=:bookingEndDate")
    boolean bookingExistsInTimeRange(long propertyId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    @Query("SELECT max(b.endDate) FROM Booking b WHERE b.property.id=:propertyId")
    LocalDate bookingInTimeRange(long propertyId);

}
