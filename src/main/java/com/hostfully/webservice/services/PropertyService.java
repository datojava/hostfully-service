package com.hostfully.webservice.services;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.properties.PropertyInfo;
import com.hostfully.webservice.models.properties.PropertyOwnerInfo;
import com.hostfully.webservice.models.properties.PropertyOwnerResponse;
import com.hostfully.webservice.models.properties.PropertyResponse;
import com.hostfully.webservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public HostfullyResponse createOwner(PropertyOwnerInfo ownerInfo) throws HostfullyWSException {
        log.info("Trying to create property owner");

        if (ownerInfo == null) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"ownerInfo"});
        }

        if (CommonUtils.isNullOrEmpty(ownerInfo.getEmail())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"email"});
        }

        if (CommonUtils.isNullOrEmpty(ownerInfo.getFirstName())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"firstName"});
        }

        if (CommonUtils.isNullOrEmpty(ownerInfo.getLastName())) {
            throw new HostfullyWSException(ErrorType.PARAMETER_MANDATORY_ERROR, new Object[]{"lastName"});
        }

        return new HostfullyResponse();
    }

    public PropertyOwnerResponse listOwners(String email) throws HostfullyWSException {

        if (CommonUtils.isNullOrEmpty(email)) {

            log.info("Getting the list of all owners");

        } else {

            log.info("Getting the list of owners by email: {}", email);

        }

        return new PropertyOwnerResponse();
    }

    public HostfullyResponse createProperty(PropertyInfo propertyInfo) {

        return new HostfullyResponse();
    }

    public PropertyResponse listProperties() {

        return new PropertyResponse();
    }
}
