package com.bank.api.techtask.service;

import com.bank.api.techtask.config.JwtAuthenticationFilter;
import com.bank.api.techtask.domain.model.Account;
import com.bank.api.techtask.domain.model.User;
import com.bank.api.techtask.exception.*;
import com.bank.api.techtask.repository.AccountRepository;
import com.bank.api.techtask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;
    private final UserSpecifications userSpecifications;
    private final AccountRepository accountRepository;
    private final ConcurrentHashMap<Long, Lock> accountLocks = new ConcurrentHashMap<>();

    @Autowired
    public UserService(UserRepository repository, JwtService jwtService, HttpServletRequest httpServletRequest,
                       UserSpecifications userSpecifications, AccountRepository accountRepository) {
        this.userRepository = repository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
        this.userSpecifications = userSpecifications;
        this.accountRepository = accountRepository;
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

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
    public Page<User> getAllUsers(Date dateOfBirth, String phoneNumber, String fullName, String email, Pageable pageable, Sort sort) {
        Specification<User> spec = Specification.where(userSpecifications.hasDateOfBirthAfter(dateOfBirth))
                .and(userSpecifications.hasPhoneNumber(phoneNumber))
                .and(userSpecifications.hasFullNameStartingWith(fullName))
                .and(userSpecifications.hasEmail(email));

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    public void moneyTransfer(Long recipientAccountId, BigDecimal amount) {
        Long userId = getUserIdFromToken();

        User senderUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        Account senderAccount = senderUser.getAccount();

        if (recipientAccountId.equals(senderAccount.getId())) {
            throw new RuntimeException("You cannot transfer money to yourself");
        }

        Account recipientAccount = accountRepository.findById(recipientAccountId)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        Lock senderLock = accountLocks.computeIfAbsent(senderAccount.getId(), k -> new ReentrantLock());
        Lock recipientLock = accountLocks.computeIfAbsent(recipientAccount.getId(), k -> new ReentrantLock());

        Lock firstLock, secondLock;
        if (senderAccount.getId() < recipientAccount.getId()) {
            firstLock = senderLock;
            secondLock = recipientLock;
        } else {
            firstLock = recipientLock;
            secondLock = senderLock;
        }

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                performTransfer(senderAccount, recipientAccount, amount);
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    private void performTransfer(Account senderAccount, Account recipientAccount, BigDecimal amount) {
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);
    }
}
