package com.bank.api.techtask.service;

import com.bank.api.techtask.domain.model.Account;
import com.bank.api.techtask.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private void increaseBalance(Account account) {
        BigDecimal maxBalance = account.getInitialBalance().multiply(BigDecimal.valueOf(2.07));
        if (account.getBalance().compareTo(maxBalance) < 0) {
            BigDecimal newBalance = account.getBalance().multiply(BigDecimal.valueOf(1.05));
            if (newBalance.compareTo(maxBalance) > 0) {
                account.setBalance(maxBalance);
            } else {
                account.setBalance(newBalance);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            increaseBalance(account);
            accountRepository.save(account);
        }
    }
}
