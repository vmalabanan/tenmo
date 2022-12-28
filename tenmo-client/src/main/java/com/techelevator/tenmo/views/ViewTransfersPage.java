package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.views.grids.TransferGrid;
import java.util.List;


public class ViewTransfersPage extends BasePage {
    // TODO: break this into two functions. Make this a void function bc transfers could be null
    public int displayTransfers(List<Transfer> transfers, int id, String message) {
        // print headers
        printHeader("Transfers");


        // print transfers
        if (transfers == null) {print("No transfers to display.");}
        else {printTransferAsGrid(transfers, id);}

        return getIntValue(message);

    }

    private void printTransferAsGrid(List<Transfer> transfers, int id) {

        TransferGrid.printTransferGrid(transfers, id);
    }

    public int getPendingTransferOption() {
        printLine("1: Approve");
        printLine("2: Reject");
        printLine("---------");

        return getSelection(2);

    }

    public Transfer approveOrRejectTransfer(List<Transfer> transfers, int transferId, int option) {
        Transfer transfer = new Transfer();

        // get transfer from list
        for (Transfer t : transfers) {
            if (t.getTransferId() == transferId) {
                transfer = t;
                break;
            }
        }

        // set transfer status
        // if user wants to approve transfer
        // set transferStatusId = 2 (Approved)
        if (option == 1) {
            transfer.setTransferStatusId(2);
        }
        // otherwise, i.e., if user wants to reject transfer
        // set transferStatusId = 3 (Rejected)
        else {
            transfer.setTransferStatusId(3);
        }

        return transfer;

    }
}
