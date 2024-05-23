package com.bank.api.techtask.domain.dto.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PhoneNumberDTO {

    @NotBlank
    @Pattern(regexp = "^\\+?\\d*$", message = "Phone number must contain only numbers and an optional leading +")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
