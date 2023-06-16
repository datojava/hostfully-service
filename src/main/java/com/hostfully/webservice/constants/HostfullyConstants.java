package com.hostfully.webservice.constants;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public interface HostfullyConstants {

    String OK = "OK";

    int HTTP_OK = 200;

    String NULL = "null";

    String NULL_BRACES = "[]";

    String MONITOR_ANNOTATION = "com.hostfully.webservice.annotations.Monitor";

    DecimalFormat DEFAULT_NUMBER_FORMAT = new DecimalFormat("#.##");

    String DEFAULT_DATE_PATTERN="MM/dd/yyyy";

    DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    String DEFAULT_LANG_CODE = "en";
}
