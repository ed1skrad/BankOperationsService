package com.bank.api.techtask.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object for a sign-up request.
 */
public class SignUpRequest {

    public static final int USERNAME_MIN_LENGTH = 5;
    public static final int USERNAME_MAX_LENGTH = 255;
    public static final int FULLNAME_MIN_LENGTH = 3;
    public static final int FULLNAME_MAX_LENGTH = 255;
    public static final int EMAIL_MIN_LENGTH = 5;
    public static final int EMAIL_MAX_LENGTH = 255;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 255;
    public static final int DATEOFBIRTH_MIN_LENGTH = 8;
    public static final int DATEOFBIRTH_MAX_LENGTH = 8;
    public static final int PHONENUBMER_MIN_LENGTH = 10;
    public static final int PHONENUBMER_MAX_LENGTH = 16;

    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH,
            message = "Имя пользователя должно содержать от "
                    + USERNAME_MIN_LENGTH + " до " + USERNAME_MAX_LENGTH + " символов")
    @NotNull(message = "Имя пользователя не может быть null")
    private String username;

    @Size(min = FULLNAME_MIN_LENGTH, max = FULLNAME_MAX_LENGTH,
            message = "ФИО должно содержать от "
                    + FULLNAME_MIN_LENGTH + " до " + FULLNAME_MAX_LENGTH + " символов")
    @NotNull(message = "ФИО не может быть null")
    private String fullName;

    @Size(min = DATEOFBIRTH_MAX_LENGTH, max = DATEOFBIRTH_MAX_LENGTH,
            message = "Длина даты рождения должна быть от " + DATEOFBIRTH_MIN_LENGTH
                    + " до " + DATEOFBIRTH_MAX_LENGTH + " символов")
    @NotNull(message = "Birth date cannot be null")

    private String dateOfBirth;

    @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH,
            message = "Адрес электронной почты должен содержать от "
                    + EMAIL_MIN_LENGTH + " до " + EMAIL_MAX_LENGTH + " символов")
    @NotNull(message = "Адрес электронной почты не может быть null")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH,
            message = "Длина пароля должна быть от " + PASSWORD_MIN_LENGTH
                    + " до " + PASSWORD_MAX_LENGTH + " символов")
    @NotNull(message = "Пароль не может быть null")
    private String password;

    @NotBlank
    @Size(min = PHONENUBMER_MIN_LENGTH, max = PHONENUBMER_MAX_LENGTH,
            message = "Длина номера телефона должна быть от " + PHONENUBMER_MIN_LENGTH
                    + " до " + PHONENUBMER_MAX_LENGTH + " символов")
    @NotNull(message = "Номер телефона не может быть null")
    private String phoneNumber;

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public @Size(min = FULLNAME_MIN_LENGTH, max = FULLNAME_MAX_LENGTH,
            message = "ФИО должно содержать от "
                    + FULLNAME_MIN_LENGTH + " до " + FULLNAME_MAX_LENGTH + " символов") @NotNull(message = "ФИО не может быть null") String getFullName() {
        return fullName;
    }

    public void setFullName(@Size(min = FULLNAME_MIN_LENGTH, max = FULLNAME_MAX_LENGTH,
            message = "ФИО должно содержать от "
                    + FULLNAME_MIN_LENGTH + " до " + FULLNAME_MAX_LENGTH + " символов") @NotNull(message = "ФИО не может быть null") String fullName) {
        this.fullName = fullName;
    }

    public @Size(min = DATEOFBIRTH_MAX_LENGTH, max = DATEOFBIRTH_MAX_LENGTH,
            message = "Длина даты рождения должна быть от " + DATEOFBIRTH_MIN_LENGTH
                    + " до " + DATEOFBIRTH_MAX_LENGTH + " символов") @NotNull(message = "Birth date cannot be null") String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@Size(min = DATEOFBIRTH_MAX_LENGTH, max = DATEOFBIRTH_MAX_LENGTH,
            message = "Длина даты рождения должна быть от " + DATEOFBIRTH_MIN_LENGTH
                    + " до " + DATEOFBIRTH_MAX_LENGTH + " символов") @NotNull(message = "Birth date cannot be null") String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @NotBlank @Size(min = PHONENUBMER_MIN_LENGTH, max = PHONENUBMER_MAX_LENGTH,
            message = "Длина номера телефона должна быть от " + PHONENUBMER_MIN_LENGTH
                    + " до " + PHONENUBMER_MAX_LENGTH + " символов") @NotNull(message = "Номер телефона не может быть null") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank @Size(min = PHONENUBMER_MIN_LENGTH, max = PHONENUBMER_MAX_LENGTH,
            message = "Длина номера телефона должна быть от " + PHONENUBMER_MIN_LENGTH
                    + " до " + PHONENUBMER_MAX_LENGTH + " символов") @NotNull(message = "Номер телефона не может быть null") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
