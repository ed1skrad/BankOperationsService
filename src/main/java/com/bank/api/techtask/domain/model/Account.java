package com.bank.api.techtask.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonBackReference
    private User user;

    private BigDecimal balance;
    private BigDecimal initialBalance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void increaseBalance() {
        BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
        BigDecimal newBalance = balance.multiply(BigDecimal.valueOf(1.05));
        if (newBalance.compareTo(maxBalance) > 0) {
            balance = maxBalance;
        } else {
            balance = newBalance;
        }
    }
}
