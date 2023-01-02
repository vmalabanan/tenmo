package com.techelevator.tenmo.models.exceptions;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class InsufficientFundsException extends Exception {
    private BigDecimal amount;
    private BigDecimal availableBalance;

    public InsufficientFundsException(BigDecimal amount, BigDecimal availableBalance) {
        this.amount = amount;
        this.availableBalance = availableBalance;
    }

    @Override
    public String getMessage(){
        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        return "Transfer amount must be less than or equal to available balance. Available balance: " + n.format(availableBalance) + ". Transfer amount: " + n.format(amount) + ".";
    }
}
