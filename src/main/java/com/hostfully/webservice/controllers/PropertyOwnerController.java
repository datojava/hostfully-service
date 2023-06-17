package com.hostfully.webservice.controllers;

import com.hostfully.webservice.annotations.Monitor;
import com.hostfully.webservice.models.properties.PropertyOwnerResponse;
import com.hostfully.webservice.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyOwnerController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyOwnerController(final PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Monitor
    @GetMapping("property-owner/list-owners")
    public PropertyOwnerResponse listOwners() throws Exception {
        return propertyService.listOwners();
    }

}
