package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.grids.UserGrid;

import java.math.BigDecimal;
import java.util.List;

public class MakeTransferPage extends BasePage {

    public static void printUserList(List<User> users) {
        printHeader("Users");
        printListOfUsersAsGrid(users);
    }

    public static Transfer createTransferObject(User user, BigDecimal amount, String message, int transferType){
        Transfer transfer = new Transfer();

        // set transfer details
        if (transferType == 1) {transfer.setUserFrom(user);}
        else {transfer.setUserTo(user);}

        transfer.setAmount(amount);
        transfer.setTransferMessage(message);
        transfer.setTransferTypeId(transferType);

        return transfer;
    }

    private static void printListOfUsersAsGrid(List<User> users){
        UserGrid.printUserGrid(users);
    }

}

