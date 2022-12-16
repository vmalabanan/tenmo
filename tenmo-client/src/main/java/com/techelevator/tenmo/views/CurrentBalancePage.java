package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Account;

import java.math.BigDecimal;

public class CurrentBalancePage extends BasePage {

    public void displayCurrentBalance(BigDecimal balance){

        printHeader("Current Balance");
        print(String.valueOf(balance));
    }
}
