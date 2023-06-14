package com.hostfully.webservice.models.properties;

import com.hostfully.webservice.models.HostfullyResponse;

import java.util.List;

public class PropertyOwnerResponse extends HostfullyResponse {

    private List<PropertyOwnerInfo> owners;

    public List<PropertyOwnerInfo> getOwners() {
        return owners;
    }

    public void setOwners(List<PropertyOwnerInfo> owners) {
        this.owners = owners;
    }
}
