package com.hostfully.webservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hostfully.webservice.constants.ExecutionTimeType;
import com.hostfully.webservice.constants.HostfullyConstants;
import com.hostfully.webservice.models.ExecutionMetaInfo;
import com.hostfully.webservice.models.HostfullyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public class CommonUtils {

    private static volatile CommonUtils instance;

    private final static long ONE_SECOND = 1000L;
    private final static long ONE_MINUTE = 60_000L;
    private final static long ONE_HOUR = 3_600_000L;
    private final static long ONE_DAY = 86_400_000L;
    private final static long ONE_WEEK = 604_800_000L;

    private final ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {

        objectMapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public static CommonUtils getInstance() {
        if (instance == null) {
            synchronized (CommonUtils.class) {
                if (instance == null) {
                    instance = new CommonUtils();
                }
            }
        }

        return instance;
    }

    public static boolean notNull(Object val) {
        return val != null;
    }

    public static boolean isNullOrEmpty(String val) {
        return val == null || val.trim().length() == 0;
    }


    public static float round(float val) {

        return Float.parseFloat(HostfullyConstants.DEFAULT_NUMBER_FORMAT.format(val));
    }

    public static ExecutionMetaInfo handleRequestExecutionInfo(LocalDateTime requestStartedAt) {
        long executedIn = Duration.between(requestStartedAt, LocalDateTime.now(Clock.systemUTC())).toMillis();

        if (executedIn <= ONE_SECOND) {

            return new ExecutionMetaInfo()
                    .withExecutedIn(executedIn)
                    .withType(ExecutionTimeType.MILLI_SECONDS)
                    .withRequestStartedAt(requestStartedAt)
                    .withRequestCompletedAt(requestStartedAt.plus(Duration.ofMillis(executedIn)));

        } else if (executedIn <= ONE_MINUTE) {

            return new ExecutionMetaInfo()
                    .withExecutedIn(round((float) executedIn / ONE_MINUTE))
                    .withType(ExecutionTimeType.SECONDS)
                    .withRequestStartedAt(requestStartedAt)
                    .withRequestCompletedAt(requestStartedAt.plus(Duration.ofMillis(executedIn)));

        } else if (executedIn <= ONE_HOUR) {

            return new ExecutionMetaInfo()
                    .withExecutedIn(round((float) executedIn / ONE_HOUR))
                    .withType(ExecutionTimeType.MINUTES)
                    .withRequestStartedAt(requestStartedAt)
                    .withRequestCompletedAt(requestStartedAt.plus(Duration.ofMillis(executedIn)));

        } else if (executedIn <= ONE_DAY) {

            return new ExecutionMetaInfo()
                    .withExecutedIn(round((float) executedIn / ONE_DAY))
                    .withType(ExecutionTimeType.HOURS)
                    .withRequestStartedAt(requestStartedAt)
                    .withRequestCompletedAt(requestStartedAt.plus(Duration.ofMillis(executedIn)));

        } else if (executedIn <= ONE_WEEK) {

            return new ExecutionMetaInfo()
                    .withExecutedIn(round((float) executedIn / ONE_WEEK))
                    .withType(ExecutionTimeType.DAYS)
                    .withRequestStartedAt(requestStartedAt)
                    .withRequestCompletedAt(requestStartedAt.plus(Duration.ofMillis(executedIn)));

        }

        return new ExecutionMetaInfo()
                .withExecutedIn(-1L)
                .withType(ExecutionTimeType.INFINITY)
                .withRequestStartedAt(requestStartedAt)
                .withRequestCompletedAt(requestStartedAt.plus(Duration.ofMillis(executedIn)));
    }

    public String objectToJSON(Object obj) {
        try {
            if (obj != null) {
                return objectMapper.writeValueAsString(obj);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return null;
    }


    public static String extractLanguageCode(String fileName) {

        int startIndex = fileName.indexOf("_") + 1;
        int endIndex = fileName.lastIndexOf('.');

        if (startIndex == 0 || endIndex < 0 || startIndex > endIndex) {
            return HostfullyConstants.DEFAULT_LANG_CODE;
        }

        return fileName.substring(startIndex, endIndex);

    }


    public static HostfullyResponse failedResponseResult(Class<?> responseClass,
                                                         HostfullyResponse failedResponse) {

        HostfullyResponse response = null;

        try {

            response = (HostfullyResponse) responseClass.getDeclaredConstructor().newInstance();

            responseClass.getMethod("withCode", int.class).invoke(response, failedResponse.getCode());
            responseClass.getMethod("withMessage", String.class).invoke(response, failedResponse.getMessage());

        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);

        }
        return response;
    }

}
