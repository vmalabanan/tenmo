package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.grids.UserGrid;

import java.math.BigDecimal;
import java.util.List;

public class MakeTransferPage extends BasePage {

    public Transfer getTransferDetails(List<User> users, int transferType){
        printHeader("Users");
        printListOfUsersAsGrid(users);
        Transfer transfer = new Transfer();
        User user = new User();
        String prompt;

        // 1 is Request
        if (transferType == 1) {
            prompt = "Enter ID of user you are requesting from (0 to cancel): ";
        }
        // Otherwise, (i.e., 2) is Send
        else {
            prompt = "Enter ID of user you are sending to (0 to cancel): ";
        }

        int id = getIntValue(prompt);
        BigDecimal amount = BigDecimal.valueOf(getIntValue("Enter amount: "));
        String message = getValue("What's it for? ");

        // set transfer details
        user.setId(id);
        transfer.setAmount(amount);
        transfer.setTransferTypeId(transferType);
        transfer.setTransferMessage(message);

        if (transferType == 1) {transfer.setUserFrom(user);}
        else {transfer.setUserTo(user);}

        return transfer;
    }

    private void printListOfUsers(List<User> users){

        users.forEach(user -> {
            printLine(user.getId() + " \t" + user.getUsername());
        });

    }

    private void printListOfUsersAsGrid(List<User> users){
        UserGrid.printUserGrid(users);
    }

}

