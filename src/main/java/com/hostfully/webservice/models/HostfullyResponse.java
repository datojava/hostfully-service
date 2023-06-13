package com.hostfully.webservice.models;

import com.hostfully.webservice.constants.HostfullyConstants;

public class HostfullyResponse {

    private int code = HostfullyConstants.HTTP_OK;

    private String message = HostfullyConstants.OK;

    public HostfullyResponse() {

    }

    public HostfullyResponse(int code, String message) {

        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public HostfullyResponse withCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HostfullyResponse withMessage(String message) {
        this.message = message;
        return this;
    }

}
