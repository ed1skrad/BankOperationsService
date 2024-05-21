package com.bank.api.techtask.exception;

public class PhoneNumberTakenException extends RuntimeException {
    public PhoneNumberTakenException(String message) {
        super(message);
    }
}
