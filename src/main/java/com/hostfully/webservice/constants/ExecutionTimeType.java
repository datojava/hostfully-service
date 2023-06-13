package com.hostfully.webservice.constants;

public enum ExecutionTimeType {

    MILLI_SECONDS("MS"),
    SECONDS("Seconds"),
    MINUTES("Minutes"),
    HOURS("Hours"),
    DAYS("Days"),
    INFINITY("Infinity");

    final String type;

    ExecutionTimeType(String type) {
        this.type = type;
    }

    public String getType() {

        return type;
    }

}