package com.hostfully.webservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hostfully.webservice.constants.ExecutionTimeType;
import com.hostfully.webservice.utils.CommonUtils;

import java.time.LocalDateTime;

public class ExecutionMetaInfo {

    @JsonProperty("executed-in")
    private float executedIn;

    @JsonProperty("time-type")
    private ExecutionTimeType type;

    @JsonProperty("request-started-at")
    private LocalDateTime requestStartedAt;

    @JsonProperty("request-completed-at")
    private LocalDateTime requestCompletedAt;

    public float getExecutedIn() {
        return executedIn;
    }

    public ExecutionMetaInfo withExecutedIn(float executedIn) {
        this.executedIn = executedIn;
        return this;
    }

    public ExecutionTimeType getType() {
        return type;
    }

    public ExecutionMetaInfo withType(ExecutionTimeType type) {
        this.type = type;
        return this;
    }

    public ExecutionMetaInfo withRequestStartedAt(LocalDateTime requestStartedAt) {
        this.requestStartedAt = requestStartedAt;
        return this;
    }

    public ExecutionMetaInfo withRequestCompletedAt(LocalDateTime requestCompletedAt) {
        this.requestCompletedAt = requestCompletedAt;
        return this;
    }

    public LocalDateTime getRequestStartedAt() {
        return requestStartedAt;
    }

    public LocalDateTime getRequestCompletedAt() {
        return requestCompletedAt;
    }

    public String toString(){
        return CommonUtils.getInstance().objectToJSON(this);
    }
}
