package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;

import java.util.List;

public class ViewTransfersPage extends BasePage {
    public int displayAllTransfers(List<Transfer> transfers, int id) {
        printHeader("Transfers");
        printHeader("ID\tFrom/to\tAmount");
        for (Transfer transfer : transfers) {
            printLine(getTransferAsString(transfer, id));
        }

        return getIntValue("Please enter transfer ID to view details (0 to cancel): ");

    }

    private String getTransferAsString(Transfer transfer, int id) {
        String str = transfer.getTransferId() + "\t";

        // if transfer is from current user to another user
        if (transfer.getUserIdFrom() == id) {
            str += "To: " + transfer.getUsernameTo();
        }
        // otherwise
        else {
            str += "From: " + transfer.getUsernameFrom();
        }

        str += "\t" + transfer.getAmount(); // need to format this as currency

        return str;
    }
}
