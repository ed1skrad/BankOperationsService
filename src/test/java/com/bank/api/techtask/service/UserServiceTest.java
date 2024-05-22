package com.bank.api.techtask.service;

import com.bank.api.techtask.config.JwtAuthenticationFilter;
import com.bank.api.techtask.domain.model.Account;
import com.bank.api.techtask.domain.model.User;
import com.bank.api.techtask.exception.InsufficientBalanceException;
import com.bank.api.techtask.exception.UserNotFoundException;
import com.bank.api.techtask.repository.AccountRepository;
import com.bank.api.techtask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMoneyTransferInsufficientBalance() {
        Long userId = 1L;
        Long recipientAccountId = 2L;
        BigDecimal amount = new BigDecimal("150.00");

        User senderUser = new User();
        senderUser.setId(userId);
        Account senderAccount = new Account();
        senderAccount.setId(userId);
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderUser.setAccount(senderAccount);

        Account recipientAccount = new Account();
        recipientAccount.setId(recipientAccountId);
        recipientAccount.setBalance(new BigDecimal("50.00"));

        when(httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME))
                .thenReturn("Bearer dummy-jwt-token");
        when(jwtService.extractUserId(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(senderUser));
        when(accountRepository.findById(recipientAccountId)).thenReturn(Optional.of(recipientAccount));

        assertThrows(InsufficientBalanceException.class, () -> userService.moneyTransfer(recipientAccountId, amount));

        verify(accountRepository, never()).save(senderAccount);
        verify(accountRepository, never()).save(recipientAccount);
    }

    @Test
    public void testMoneyTransferSuccess() {
        Long userId = 1L;
        Long recipientAccountId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        User senderUser = new User();
        senderUser.setId(userId);
        Account senderAccount = new Account();
        senderAccount.setId(userId);
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderUser.setAccount(senderAccount);

        Account recipientAccount = new Account();
        recipientAccount.setId(recipientAccountId);
        recipientAccount.setBalance(new BigDecimal("50.00"));

        when(httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME))
                .thenReturn("Bearer dummy-jwt-token");
        when(jwtService.extractUserId(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(senderUser));
        when(accountRepository.findById(recipientAccountId)).thenReturn(Optional.of(recipientAccount));

        userService.moneyTransfer(recipientAccountId, amount);

        assertEquals(new BigDecimal("50.00"), senderAccount.getBalance());
        assertEquals(new BigDecimal("100.00"), recipientAccount.getBalance());

        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(recipientAccount);
    }

    @Test
    public void testMoneyTransferToSelf() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("50.00");

        User senderUser = new User();
        senderUser.setId(userId);
        Account senderAccount = new Account();
        senderAccount.setId(userId);
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderUser.setAccount(senderAccount);

        when(jwtService.extractUserId(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(senderUser));

        assertThrows(RuntimeException.class, () -> userService.moneyTransfer(userId, amount));

        verify(accountRepository, never()).save(senderAccount);
    }

    @Test
    public void testMoneyTransferRecipientNotFound() {
        Long userId = 1L;
        Long recipientAccountId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        User senderUser = new User();
        senderUser.setId(userId);
        Account senderAccount = new Account();
        senderAccount.setId(userId);
        senderAccount.setBalance(new BigDecimal("100.00"));
        senderUser.setAccount(senderAccount);

        when(jwtService.extractUserId(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(senderUser));
        when(accountRepository.findById(recipientAccountId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.moneyTransfer(recipientAccountId, amount));

        verify(accountRepository, never()).save(senderAccount);
    }

    @Test
    public void testMoneyTransferUserNotFound() {
        Long userId = 1L;
        Long recipientAccountId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        when(httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME))
                .thenReturn("Bearer dummy-jwt-token");
        when(jwtService.extractUserId(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.moneyTransfer(recipientAccountId, amount));

        verify(accountRepository, never()).save(any(Account.class));
    }
}
