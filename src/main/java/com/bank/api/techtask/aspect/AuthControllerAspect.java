package com.bank.api.techtask.aspect;

import com.bank.api.techtask.controller.AuthController;
import com.bank.api.techtask.domain.dto.SignInRequest;
import com.bank.api.techtask.domain.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * This aspect logs all method calls in the {@link AuthController} class.
 */

@Aspect
@Component
public class AuthControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthControllerAspect.class);

    /**
     * Logs a message before each method call in the {@link AuthController} class.
     *
     * @param joinPoint the join point representing the method call
     */
    @Before("execution(* com.bank.api.techtask.controller.AuthController.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Calling method: {}", methodName);
        switch (methodName) {
            case "signUp" -> {
                SignUpRequest signUpRequest = (SignUpRequest) args[0];
                LOGGER.info("Signing up user: {}", signUpRequest.getUsername());
            }
            case "signIn" -> {
                SignInRequest signInRequest = (SignInRequest) args[0];
                LOGGER.info("Signing in user: {}", signInRequest.getUsername());
            }
            case "logout" -> {
                HttpServletRequest request = (HttpServletRequest) args[0];
                String username = (String) request.getAttribute("username");
                LOGGER.info("Logging out user: {}", username);
            }
            default -> LOGGER.info("No specific logging for method: {}", methodName);
        }
    }
}