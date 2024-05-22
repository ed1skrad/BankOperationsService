package com.bank.api.techtask.service;

import com.bank.api.techtask.domain.model.Account;
import com.bank.api.techtask.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            account.increaseBalance();
            accountRepository.save(account);
        }
    }
}
