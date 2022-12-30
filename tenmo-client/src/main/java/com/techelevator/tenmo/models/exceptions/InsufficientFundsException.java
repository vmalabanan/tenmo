package com.techelevator.tenmo.models.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends Exception {
    private BigDecimal amount;
    private BigDecimal availableBalance;

    public InsufficientFundsException(BigDecimal amount, BigDecimal availableBalance) {
        this.amount = amount;
        this.availableBalance = availableBalance;
    }

    @Override
    public String getMessage(){

        return "Amount must be less than or equal to available balance. Available balance: $" + availableBalance + ". Amount entered: $" + amount + ".";
    }
}
