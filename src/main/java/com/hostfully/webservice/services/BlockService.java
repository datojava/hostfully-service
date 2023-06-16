package com.hostfully.webservice.services;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.entities.Block;
import com.hostfully.webservice.entities.Property;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.blocks.BlockInfo;
import com.hostfully.webservice.repositories.BlockRepository;
import com.hostfully.webservice.repositories.PropertyRepository;
import com.hostfully.webservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class BlockService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BlockRepository blockRepository;

    private final PropertyRepository propertyRepository;

    public BlockService(final BlockRepository blockRepository,
                        final PropertyRepository propertyRepository) {
        this.blockRepository = blockRepository;
        this.propertyRepository = propertyRepository;
    }

    public HostfullyResponse createBlock(BlockInfo blockInfo) throws HostfullyWSException {

        log.info("Creating block...");

        validateBlock(blockInfo);

        Optional<Property> selectedProp = propertyRepository.findById(blockInfo.getPropertyId());

        if (selectedProp.isEmpty()) {
            throw new HostfullyWSException(ErrorType.PROPERTY_NOT_FOUND, new Object[]{blockInfo.getPropertyId()});
        }

        Block block = new Block();

        try {
            block.setStartDate(CommonUtils.toDate(blockInfo.getStartDate()));
            block.setEndDate(CommonUtils.toDate(blockInfo.getEndDate()));
        } catch (DateTimeParseException e) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        block.setProperty(selectedProp.get());

        log.info("Property Name: {}", block.getProperty().getName());
        log.info("Start Date:    {}", blockInfo.getStartDate());
        log.info("End Date:      {}", blockInfo.getEndDate());

        blockRepository.save(block);

        log.info("Block created");

        return new HostfullyResponse();
    }

    public HostfullyResponse updateBlock(BlockInfo blockInfo) throws HostfullyWSException {

        log.info("Updating block...");

        validateBlock(blockInfo);

        if (blockInfo.getId() == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"id"});
        }

        if (blockInfo.getPropertyId() == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"propertyId"});
        }

        Optional<Block> selectedBlockOpt = blockRepository.findById(blockInfo.getId());

        if (selectedBlockOpt.isEmpty()) {
            throw new HostfullyWSException(ErrorType.BLOCK_NOT_FOUND, new Object[]{blockInfo.getId()});
        }

        Optional<Property> selectedProp = propertyRepository.findById(blockInfo.getPropertyId());

        if (selectedProp.isEmpty()) {
            throw new HostfullyWSException(ErrorType.PROPERTY_NOT_FOUND, new Object[]{blockInfo.getPropertyId()});
        }


        Block existingBlock = selectedBlockOpt.get();

        try {
            existingBlock.setStartDate(CommonUtils.toDate(blockInfo.getStartDate()));
            existingBlock.setEndDate(CommonUtils.toDate(blockInfo.getEndDate()));
        } catch (DateTimeParseException e) {
            throw new HostfullyWSException(ErrorType.ILLEGAL_PARAMETER_VALUE_ERROR, new Object[]{"startDate or endDate"});
        }

        existingBlock.setProperty(selectedProp.get());

        log.info("Property Name: {}", existingBlock.getProperty().getName());
        log.info("Start Date:    {}", blockInfo.getStartDate());
        log.info("End Date:      {}", blockInfo.getEndDate());

        blockRepository.save(existingBlock);

        log.info("Block updated");

        return new HostfullyResponse();

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
