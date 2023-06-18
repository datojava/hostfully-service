package com.hostfully.webservice.repositories;

import com.hostfully.webservice.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {

    @Query("SELECT count(b.id)>0 FROM Block b WHERE b.property.id=:propertyId AND b.startDate<=:startDate AND b.endDate>=:endDate")
    boolean propertyBlockedInTimeRange(long propertyId, LocalDate startDate, LocalDate endDate);

}
