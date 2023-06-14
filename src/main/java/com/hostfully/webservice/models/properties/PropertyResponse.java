package com.hostfully.webservice.models.properties;

import com.hostfully.webservice.models.HostfullyResponse;

import java.util.List;

public class PropertyResponse extends HostfullyResponse {

    private List<PropertyInfo> properties;

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInfo> properties) {
        this.properties = properties;
    }
}
