package com.bank.api.techtask.aspect;
import com.bank.api.techtask.exception.UserNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class ServiceAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Before("execution(* com.bank.api.techtask.service..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.bank.api.techtask.service..*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.bank.api.techtask.service..*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Exception in method: {} with cause: {}", joinPoint.getSignature().toShortString(), error.getMessage(), error);
    }

    @Around("execution(* com.bank.api.techtask.service..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logger.info("Method {} execution start", joinPoint.getSignature().toShortString());

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Method {} execution end, elapsed time: {} ms", joinPoint.getSignature().toShortString(), elapsedTime);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in method: {}", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().toShortString());
            throw e;
        } catch (UserNotFoundException e) {
            logger.error("User not found!");
            throw e;
        }
    }

}
