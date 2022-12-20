package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import java.math.BigDecimal;
import java.util.List;

public class MakeTransferPage extends BasePage {

    public Transfer getTransferDetailsSend(List<User> users){
        printingListOfUsers(users);
        Transfer transfer = new Transfer();

        int id = getIntValue("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amount = BigDecimal.valueOf(getIntValue("Enter amount: "));

        transfer.setAmount(amount);
        transfer.setUserIdTo(id);
        transfer.setTransferTypeId(2); // 2 is sending

        return transfer;
    }

    private void printingListOfUsers(List<User> users ){
        printHeader("Users");
        printLine("ID\t\t Name");
        users.forEach(user -> {
            printLine(user.getId() + " \t" + user.getUsername());
        });

    }

    public Transfer getTransferDetailsRequest(List<User> users){
        printingListOfUsers(users);
        Transfer transfer = new Transfer();

        int id = getIntValue("Enter ID of user you are requesting from (0 to cancel): ");
        BigDecimal amount = BigDecimal.valueOf(getIntValue("Enter amount: "));

        transfer.setAmount(amount);
        transfer.setUserIdFrom(id);
        transfer.setTransferTypeId(1); // 1 is requesting

        return transfer;
    }

}

