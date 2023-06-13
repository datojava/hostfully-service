package com.hostfully.webservice.aspects;

import com.hostfully.webservice.constants.ErrorType;
import com.hostfully.webservice.constants.HostfullyConstants;
import com.hostfully.webservice.exceptions.HostfullyWSException;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.utils.CommonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Configuration
@SuppressWarnings("unused")
public class MonitorAspect {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Around("@annotation(" + HostfullyConstants.MONITOR_ANNOTATION + ")")
    public HostfullyResponse around(ProceedingJoinPoint joinPoint) throws Throwable {

        try {

            log.info("Proceeding method: {}", joinPoint.getSignature());

            LocalDateTime requestStartedAt = LocalDateTime.now(Clock.systemUTC());

            HostfullyResponse response = (HostfullyResponse) joinPoint.proceed();

            log.info("Request processed: {}", CommonUtils.handleRequestExecutionInfo(requestStartedAt));

            return response;

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return CommonUtils.failedResponseResult(((MethodSignature) joinPoint.getSignature()).getReturnType(), handle(ex, new HostfullyResponse()));

        }
    }

    public static <T extends HostfullyResponse> T handle(Exception ex, T response) {

        if (ex instanceof HostfullyWSException) {

            response.withCode(Objects.requireNonNullElse(((HostfullyWSException) ex).getError(), ErrorType.GENERAL_ERROR).getCode());
            response.withMessage(((HostfullyWSException) ex).errorMessage());

        }  else {

            response.withCode(ErrorType.errorTypeBy(ex.getMessage()).getCode());
            response.withMessage(ErrorType.errorTypeBy(ex.getMessage()).errorMessage());

        }
        return response;
    }


}
