package com.bank.api.techtask.service;

import com.bank.api.techtask.config.JwtAuthenticationFilter;
import com.bank.api.techtask.domain.model.User;
import com.bank.api.techtask.exception.DeleteException;
import com.bank.api.techtask.exception.EmailInUseException;
import com.bank.api.techtask.exception.PhoneNumberTakenException;
import com.bank.api.techtask.exception.UserNotFoundException;
import com.bank.api.techtask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service class for handling user operations.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;
    private final UserSpecifications userSpecifications;

    /**
     *Constructor.
     */
    @Autowired
    public UserService(UserRepository repository, JwtService jwtService, HttpServletRequest httpServletRequest,
                       UserSpecifications userSpecifications) {
        this.userRepository = repository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
        this.userSpecifications = userSpecifications;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve.
     * @return the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

    }

    /**
     *Constructor.
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    private Long getUserIdFromToken() {
        String authHeader = httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(JwtAuthenticationFilter.BEARER_PREFIX)) {
            throw new RuntimeException("JWT token not found in the request header.");
        }

        String jwt = authHeader.substring(JwtAuthenticationFilter.BEARER_PREFIX.length());
        return jwtService.extractUserId(jwt);
    }

    @Transactional
    public void deleteUserEmail() {
        Long userId = getUserIdFromToken();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        if (user.getPhoneNumber() == null) {
            throw new DeleteException("User has no email or phone number. Cannot delete the email.");
        }

        if (user.getEmail() == null) {
            throw new DeleteException("User already doesn't have an email address.");
        }

        user.setEmail(null);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserPhoneNumber() {
        Long userId = getUserIdFromToken();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        if (user.getEmail() == null) {
            throw new DeleteException("User has no email or phone number. Cannot delete the phone number.");
        }

        if (user.getPhoneNumber() == null) {
            throw new DeleteException("User already doesn't have a phone number.");
        }

        user.setPhoneNumber(null);
        userRepository.save(user);
    }

    @Transactional
    public void updatePhoneNumber(String phoneNumber) {
        Long userId = getUserIdFromToken();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        String parsedPhoneNumber = phoneNumber.replaceAll("[^\\d+]", "");

        if (userRepository.existsByPhoneNumber(parsedPhoneNumber)) {
            throw new PhoneNumberTakenException("User with this phone number already exists.");
        }

        user.setPhoneNumber(parsedPhoneNumber);
        userRepository.save(user);
    }

    @Transactional
    public void updateEmail(String email) {
        Long userId = getUserIdFromToken();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        if (userRepository.existsByEmail(email)) {
            throw new EmailInUseException("User with this email already exists.");
        }

        user.setEmail(email);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(Date dateOfBirth, String phoneNumber, String fullName, String email) {
        Specification<User> spec = Specification.where(userSpecifications.hasDateOfBirthAfter(dateOfBirth))
                .and(userSpecifications.hasPhoneNumber(phoneNumber))
                .and(userSpecifications.hasFullNameStartingWith(fullName))
                .and(userSpecifications.hasEmail(email));

        return userRepository.findAll(spec);
    }
}
