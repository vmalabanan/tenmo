package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.views.TenmoLogo;

public class WelcomePage extends BasePage {

    public static void printLogo()
    {
        printHeader("Welcome to TEnmo!");
        TenmoLogo.printTenmoLogo();
        printLine();
        pause();
    }

    public static void printLoginMenu()
    {
        printHeader("Menu");
        printLine("1: Register");
        printLine("2: Login");
        printLine("0: Exit");
        printLine();
    }
}
