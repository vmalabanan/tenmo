package com.techelevator.tenmo.models.exceptions;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class InvalidAmountException extends Exception {
    private BigDecimal amount;

    public InvalidAmountException(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage(){
        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        return "Amount must be greater than $0. Amount entered: " + n.format(amount) + ".";
    }
}
