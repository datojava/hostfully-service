package com.hostfully.webservice.constants;

import com.hostfully.webservice.utils.MessageBundleReader;

import java.text.MessageFormat;
import java.util.Arrays;

public enum ErrorType {

    UNKNOWN_ERROR(999, "com.hostfully.webservice.unknown.error"),
    GENERAL_ERROR(1000, "com.hostfully.webservice.general.error"),
    PARAMETER_MANDATORY_ERROR(1001, "com.hostfully.webservice.param.mandatory.error");


    private final int code;

    private final String errorKey;

    private final MessageBundleReader msgReader = MessageBundleReader.getInstance();

    ErrorType(int code, String errorKey) {
        this.code = code;
        this.errorKey = errorKey;
    }

    public int getCode() {
        return code;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public String errorMessage() {
        return msgReader.getMessage(errorKey);
    }

    public String errorMessage(Object... params) {
        if (params == null || params.length == 0) {
            return msgReader.getMessage(errorKey);
        }
        return MessageFormat.format(msgReader.getMessage(errorKey), params);
    }

    public static ErrorType errorTypeBy(int errorCode) {

        return Arrays.stream(ErrorType.values()).filter(errorType -> errorType.getCode() == errorCode).findFirst().orElse(ErrorType.UNKNOWN_ERROR);

    }

    public static ErrorType errorTypeBy(String text) {

        return Arrays.stream(ErrorType.values()).filter(e -> e.errorMessage().equalsIgnoreCase(text)).findFirst().orElse(ErrorType.GENERAL_ERROR);

    }

}
