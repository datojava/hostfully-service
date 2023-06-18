package com.hostfully.webservice.services;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.entities.Block;
import com.hostfully.webservice.entities.Property;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.blocks.BlockInfo;
import com.hostfully.webservice.models.blocks.CreateUpdateBlockResponse;
import com.hostfully.webservice.repositories.BlockRepository;
import com.hostfully.webservice.repositories.BookingRepository;
import com.hostfully.webservice.repositories.PropertyRepository;
import com.hostfully.webservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class BlockService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BlockRepository blockRepository;

    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;


    @Autowired
    public BlockService(final BlockRepository blockRepository,
                        final PropertyRepository propertyRepository,
                        BookingRepository bookingRepository) {
        this.blockRepository = blockRepository;
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
    }

    public CreateUpdateBlockResponse createOrUpdateBlock(BlockInfo blockInfo) throws HostfullyWSException {
        validateBlock(blockInfo);

        Block block;

        if (blockInfo.getId() == null) {

            log.info("Creating block...");

            block = new Block();

        } else {

            log.info("Updating block...");

            Optional<Block> selectedBlockOpt = blockRepository.findById(blockInfo.getId());

            if (selectedBlockOpt.isEmpty()) {
                throw new HostfullyWSException(ErrorType.BLOCK_NOT_FOUND, new Object[]{blockInfo.getId()});
            }

            block = selectedBlockOpt.get();
        }

        if (blockInfo.getPropertyId() == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"propertyId"});
        }

        try {
            block.setStartDate(CommonUtils.toDate(blockInfo.getStartDate()));
            block.setEndDate(CommonUtils.toDate(blockInfo.getEndDate()));
        } catch (DateTimeParseException e) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        if (block.getStartDate().isEqual(block.getEndDate()) || block.getStartDate().isAfter(block.getEndDate())) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        Optional<Property> selectedProp = propertyRepository.findById(blockInfo.getPropertyId());

        if (selectedProp.isEmpty()) {
            throw new HostfullyWSException(ErrorType.PROPERTY_NOT_FOUND, new Object[]{blockInfo.getPropertyId()});
        }

        block.setProperty(selectedProp.get());

        if (bookingRepository.bookingExistsInTimeRange(block.getProperty().getId(), block.getStartDate(), block.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PROPERTY_ALREADY_BOOKED);
        }

        if (blockRepository.propertyBlockedInTimeRange(block.getProperty().getId(), block.getStartDate(), block.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PROPERTY_BLOCKED);
        }

        log.info("Property Name: {}", block.getProperty().getName());
        log.info("Start Date:    {}", blockInfo.getStartDate());
        log.info("End Date:      {}", blockInfo.getEndDate());

        CreateUpdateBlockResponse response = new CreateUpdateBlockResponse();

        response.setId(blockRepository.save(block).getId());

        return response;
    }

    private void validateBlock(BlockInfo blockInfo) throws HostfullyWSException {
        if (blockInfo == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"blockInfo"});
        }

        if (blockInfo.getPropertyId() == null || blockInfo.getPropertyId() <= 0L) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"propertyId"});
        }

        if (CommonUtils.isNullOrEmpty(blockInfo.getStartDate())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"startDate"});
        }

        if (CommonUtils.isNullOrEmpty(blockInfo.getEndDate())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"endDate"});
        }
    }

    public HostfullyResponse deleteBlock(Long blockId) throws HostfullyWSException {

        if (blockId == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"id"});
        }

        Optional<Block> selectedBlockOpt = blockRepository.findById(blockId);

        if (selectedBlockOpt.isEmpty()) {
            throw new HostfullyWSException(ErrorType.BOOKING_NOT_FOUND, new Object[]{blockId});
        }

        log.info("Deleting block by id: {}", blockId);

        blockRepository.delete(selectedBlockOpt.get());

        return new HostfullyResponse();

    }
}
