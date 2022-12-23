package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.views.constants.ColorCodes;

import java.text.NumberFormat;
import java.util.List;

public class ViewTransfersPage extends BasePage {
    public int displayAllTransfers(List<Transfer> transfers, int id) {
        // print headers
        printHeader("Transfers");
        // iterate through transfers list and print each on a line
        for (Transfer transfer : transfers) {
            printLine(getTransferAsString(transfer, id));
//            printTransferAsGrid(transfer, id);
        }

        return getIntValue("Please enter transfer ID to view details (0 to cancel): ");

    }

    // we are using this method instead of overriding toString() in Transfer class
    // because we need to know who the logged-in user is to determine whether each transaction
    // is to/from the current user
    private String getTransferAsString(Transfer transfer, int id) {
        // format transferId to have a width of 10, justified left
        String str = String.format("%-10d", transfer.getTransferId());

        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        // if transfer is FROM current user,
        // we want to display usernameTo
        // and format the amount as a negative
        if (transfer.getUserIdFrom() == id) {
            // format usernameTo to have a width of 15, justified left
            str += String.format("%-15s", transfer.getUsernameTo());
            // add red font and a leading - sign for amount
            // format amount to have a width of 11, justified right
            str += ColorCodes.RED + String.format("%11s", "-" + n.format(transfer.getAmount())) + ColorCodes.RESET;

        }
        // otherwise (i.e., if transfer is TO current user),
        // we want to display usernameFrom
        else {
            // format usernameFrom to have a width of 15, justified left
            str += String.format("%-15s", transfer.getUsernameFrom());
            // format amount to have a width of 11, justified right
            str += String.format("%11s", n.format(transfer.getAmount()));

        }



        return str;
    }
}
