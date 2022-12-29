package com.techelevator.tenmo.views.pages;

import java.math.BigDecimal;

public class CurrentBalancePage extends BasePage {

    public static void displayCurrentBalance(BigDecimal balance){

        printHeader("Current Balance");
        printLine(String.valueOf(balance));
    }
}
