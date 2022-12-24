package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.grids.UserGrid;

import java.math.BigDecimal;
import java.util.List;

public class MakeTransferPage extends BasePage {

    public Transfer getTransferDetailsSend(List<User> users){
        printHeader("Users");
        printListOfUsersAsGrid(users);
        Transfer transfer = new Transfer();
        User user = new User();

        int id = getIntValue("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amount = BigDecimal.valueOf(getIntValue("Enter amount: "));

        transfer.setAmount(amount);
        transfer.setTransferTypeId(2); // 2 is sending
        user.setId(id);
        transfer.setUserTo(user);

        return transfer;
    }

    public Transfer getTransferDetailsRequest(List<User> users){
        printListOfUsersAsGrid(users);
        Transfer transfer = new Transfer();
        User user = new User();

        int id = getIntValue("Enter ID of user you are requesting from (0 to cancel): ");
        BigDecimal amount = BigDecimal.valueOf(getIntValue("Enter amount: "));

        transfer.setAmount(amount);
        transfer.setTransferTypeId(1); // 1 is requesting
        user.setId(id);
        transfer.setUserFrom(user);

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

