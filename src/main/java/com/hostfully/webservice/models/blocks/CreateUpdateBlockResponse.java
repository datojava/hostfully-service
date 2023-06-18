package com.hostfully.webservice.models.blocks;

import com.hostfully.webservice.models.HostfullyResponse;

public class CreateUpdateBlockResponse extends HostfullyResponse {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
