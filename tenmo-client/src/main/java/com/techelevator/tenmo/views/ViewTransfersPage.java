package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.views.grids.TransferGrid;
import java.util.List;

// TODO: break this into two functions. Make this a void function bc transfers could be null
public class ViewTransfersPage extends BasePage {
    public int displayAllTransfers(List<Transfer> transfers, int id) {
        // print headers
        printHeader("Transfers");

        // print transfers
        if (transfers == null) {print("No transfers to display.");}
        else {printTransferAsGrid(transfers, id);}

        return getIntValue("Please enter transfer ID to view details (0 to cancel): ");

    }

    private void printTransferAsGrid(List<Transfer> transfers, int id) {
        TransferGrid.printTransferGrid(transfers, id);
    }
}
