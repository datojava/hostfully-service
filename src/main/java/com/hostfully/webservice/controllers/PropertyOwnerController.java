package com.hostfully.webservice.controllers;

import com.hostfully.webservice.annotations.Monitor;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.properties.PropertyOwnerInfo;
import com.hostfully.webservice.models.properties.PropertyOwnerResponse;
import com.hostfully.webservice.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyOwnerController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyOwnerController(final PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Monitor
    @PostMapping("property-owner/create")
    public HostfullyResponse createOwner(@RequestBody PropertyOwnerInfo ownerInfo) throws Exception {
        return propertyService.createOwner(ownerInfo);
    }

    @Monitor
    @GetMapping("property-owner/list-owners")
    public PropertyOwnerResponse listOwners(@RequestParam("email") String email) throws Exception {
        return propertyService.listOwners(email);
    }

}
