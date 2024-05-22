package com.bank.api.techtask.service;

import com.bank.api.techtask.config.JwtAuthenticationFilter;
import com.bank.api.techtask.domain.model.User;
import com.bank.api.techtask.exception.DeleteException;
import com.bank.api.techtask.exception.UserNotFoundException;
import com.bank.api.techtask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user operations.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;

    /**
     *Constructor.
     */
    @Autowired
    public UserService(UserRepository repository, JwtService jwtService, HttpServletRequest httpServletRequest) {
        this.userRepository = repository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
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

    @Transactional
    public void deleteUserEmail() {
        String authHeader = httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(JwtAuthenticationFilter.BEARER_PREFIX)) {
            throw new RuntimeException("JWT token not found in the request header.");
        }

        String jwt = authHeader.substring(JwtAuthenticationFilter.BEARER_PREFIX.length());
        Long userId = jwtService.extractUserId(jwt);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        if (user.getPhoneNumber() == null) {
            throw new DeleteException("User has no email or phone number. Cannot delete the email.");
        }

        if(user.getEmail() == null){
            throw new DeleteException("User already don't have an email address.");
        }


        if (user.getEmail() != null) {
            user.setEmail(null);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteUserPhoneNumber() {
        String authHeader = httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(JwtAuthenticationFilter.BEARER_PREFIX)) {
            throw new RuntimeException("JWT token not found in the request header.");
        }

        String jwt = authHeader.substring(JwtAuthenticationFilter.BEARER_PREFIX.length());
        Long userId = jwtService.extractUserId(jwt);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        if (user.getEmail() == null) {
            throw new DeleteException("User has no email or phone number. Cannot delete the phone number.");
        }

        if(user.getPhoneNumber() == null){
            throw new DeleteException("User already don't have an phone number.");
        }

        if (user.getPhoneNumber() != null) {
            user.setPhoneNumber(null);
            userRepository.save(user);
        }
    }
}
