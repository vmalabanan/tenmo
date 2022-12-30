package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.exceptions.InvalidTransferIdException;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.views.grids.TransferDetailsGrid;

import java.util.List;

public class ViewTransferDetailsPage extends BasePage {

    public static void displayTransferDetails(List<Transfer> transfers, int transferId, int id) {
        Transfer transfer = null;

        // check to see if the requested transfer is in the list
        try {
            for (Transfer t : transfers) {
                if (t.getTransferId() == transferId) {
                    transfer = t;

                    break;
                }
            }

            if (transfer == null) {
                throw new InvalidTransferIdException(transferId);
            }

            printHeader("Transfer Details");
            printLine("Transfer ID " + transfer.getTransferId());
            TransferDetailsGrid.printTransferDetails(transfer, id);

        } catch (InvalidTransferIdException e) {
            // TODO - do something with exception
        }

        pause();

    }
}
