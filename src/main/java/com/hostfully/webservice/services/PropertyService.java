package com.hostfully.webservice.services;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.entities.Property;
import com.hostfully.webservice.entities.PropertyOwner;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.properties.PropertyInfo;
import com.hostfully.webservice.models.properties.PropertyOwnerInfo;
import com.hostfully.webservice.models.properties.PropertyOwnerResponse;
import com.hostfully.webservice.models.properties.PropertyResponse;
import com.hostfully.webservice.repositories.PropertyOwnerRepository;
import com.hostfully.webservice.repositories.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PropertyOwnerRepository propertyOwnerRepository;

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(final PropertyOwnerRepository propertyOwnerRepository, final PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyOwnerRepository = propertyOwnerRepository;
    }

    public PropertyOwnerResponse listOwners() throws HostfullyWSException {
        log.info("Getting the list of all owners");

        PropertyOwnerResponse ownerResponse = new PropertyOwnerResponse();
        ownerResponse.setOwners(new ArrayList<>());

        List<PropertyOwner> owners = propertyOwnerRepository.findAll();

        if (owners.isEmpty()) {
            throw new HostfullyWSException(ErrorType.DATA_NOT_FOUND);
        }

        PropertyOwnerInfo ownerInfo;

        for (PropertyOwner propertyOwner : owners) {

            ownerInfo = new PropertyOwnerInfo();

            ownerInfo.setId(propertyOwner.getId());
            ownerInfo.setEmail(propertyOwner.getEmail());
            ownerInfo.setFirstName(propertyOwner.getFirstName());
            ownerInfo.setLastName(propertyOwner.getLastName());

            ownerResponse.getOwners().add(ownerInfo);

        }

        return ownerResponse;
    }

    public PropertyResponse listProperties() throws HostfullyWSException {

        log.info("Listing all properties");

        PropertyResponse response = new PropertyResponse();
        response.setProperties(new ArrayList<>());

        List<Property> properties = propertyRepository.findAll();

        if (properties.isEmpty()) {
            throw new HostfullyWSException(ErrorType.DATA_NOT_FOUND);
        }

        PropertyInfo propInfo;

        for (Property prop : properties) {

            propInfo = new PropertyInfo();

            propInfo.setId(prop.getId());
            propInfo.setAddress(prop.getAddress());
            propInfo.setName(prop.getName());

            response.getProperties().add(propInfo);
        }

        return response;
    }
}
