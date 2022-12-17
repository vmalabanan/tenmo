package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import java.math.BigDecimal;
import java.util.List;

public class MakeTransferPage extends BasePage {

    public Transfer getTransferDetails(List<User> users){
        printHeader("Users");
        printLine("ID\t\t Name");
        users.forEach(user -> {
            printLine(user.getId() + " \t" + user.getUsername());
        });

        Transfer transfer = new Transfer();

        int id = getIntValue("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amount = BigDecimal.valueOf(getIntValue("Enter amount: "));

        transfer.setAmount(amount);
        transfer.setUserId(id);

        return transfer;
    }


}

