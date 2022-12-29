package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.grids.MainMenuGrid;

import java.math.BigDecimal;

public class MainMenuPage extends BasePage {
    public static void printMenu(User user, BigDecimal balance)
    {
        printHeader("Main menu");
        MainMenuGrid.printMainMenuGrid(user, balance);
    }

}
