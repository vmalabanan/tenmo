package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.exceptions.IncorrectTransferIdException;
import com.techelevator.tenmo.models.Transfer;

import java.util.List;

public class ViewTransferDetailsPage extends BasePage {

    public void displayTransferDetails(List<Transfer> transfers, int transferId) {
        Transfer transfer = null;

        // check to see if
        try {
            for (Transfer t : transfers) {
                if (t.getTransferId() == transferId) {
                    transfer = t;
                    break;
                }
            }

            if (transfer == null) {
                throw new IncorrectTransferIdException();
            }

            printHeader("Transfer Details");
            printLine("ID: " + transfer.getTransferId());
            printLine("From: " + transfer.getUserFrom().getUsername());
            printLine("To: " + transfer.getUserTo().getUsername());
            printLine("Type: " + transfer.getTransferTypeDesc());
            printLine("Status: " + transfer.getTransferStatusDesc());
            printLine("Amount: $" + transfer.getAmount());
        } catch (IncorrectTransferIdException e) {
            // TODO - do something with exception
        }

        getValue("Press enter to continue");

    }
}
