package com.example.chatserver.aspect;

import com.example.chatserver.producer.LogSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogSender logSender;
    private final ObjectMapper objectMapper;

    @Before("execution(* com.example.chatserver.service..*(..))")
    public void logBefore(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        String arguments = Arrays.toString(joinPoint.getArgs());
        String logMessage = "Before method: " + methodName + " - Arguments: " + arguments;
        log.info(logMessage);
        logSender.sendMessage(logMessage);
    }

    @AfterReturning(pointcut = "execution(* com.example.chatserver.service..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String returnValue = objectMapper.writeValueAsString(result);
        String logMessage = "After method: " + methodName + " - Return value: " + returnValue;
        log.info(logMessage);
        logSender.sendMessage(logMessage);
    }

    @AfterThrowing(pointcut = "execution(* com.example.chatserver.service..*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String logMessage = "Exception in method: " + methodName + " - Exception: " + exception.getMessage();
        log.info(logMessage);
        logSender.sendMessage(logMessage);
    }
}
