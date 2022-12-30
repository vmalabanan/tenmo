package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.views.grids.TransferGrid;

import java.util.List;


public class ViewTransfersPage extends BasePage {

    public static void displayTransfers(List<Transfer> transfers, int id, String header) {
        // print headers
        printHeader(header);

        // print transfers
       printTransferAsGrid(transfers, id);

    }

    private static void printTransferAsGrid(List<Transfer> transfers, int id) {

        TransferGrid.printTransferGrid(transfers, id);
    }

    public static int getApproveOrRejectChoice() {
        printLine("1: Approve");
        printLine("2: Reject");
        printLine("0: Cancel");
        printLine("---------");

        return getSelection();

    }

}
