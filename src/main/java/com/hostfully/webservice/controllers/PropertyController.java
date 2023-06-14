package com.hostfully.webservice.controllers;

import com.hostfully.webservice.annotations.Monitor;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.properties.PropertyInfo;
import com.hostfully.webservice.models.properties.PropertyResponse;
import com.hostfully.webservice.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(final PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Monitor
    @GetMapping("property/list-properties")
    public PropertyResponse listProperties() throws Exception {
        return propertyService.listProperties();
    }


}
