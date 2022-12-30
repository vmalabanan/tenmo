package com.techelevator.tenmo.models.exceptions;

import java.math.BigDecimal;

public class InvalidAmountException extends Exception {
    private BigDecimal amount;

    public InvalidAmountException(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage(){


        return "Amount must be greater than $0. Amount entered: $" + amount + ".";
    }
}
