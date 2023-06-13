package com.hostfully.webservice.exceptions;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.utils.CommonUtils;

public class HostfullyWSException extends Exception{

    private ErrorType error;

    private String message;

    private Object[] params;

    public HostfullyWSException(String message) {
        super(message);
        this.message = message;
    }

    public HostfullyWSException(ErrorType error, String message) {
        super(message);
        this.message = message;
        this.error = error;
    }

    public HostfullyWSException(ErrorType error) {
        super(error.errorMessage());
        this.error = error;
    }

    public HostfullyWSException(ErrorType error, Object[] params) {
        super(error.errorMessage(params));
        this.error = error;
        this.params = params;
    }

    public ErrorType getError() {
        return error;
    }

    public String errorMessage() {

        if (CommonUtils.notNull(error)) {

            if (CommonUtils.notNull(params)) {

                return error.errorMessage(params);

            } else {

                if (CommonUtils.isNullOrEmpty(message)) {

                    return error.errorMessage();

                } else {

                    return message;

                }

            }
        }

        return message;
    }


}
