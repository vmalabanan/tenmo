package com.techelevator.tenmo.models.exceptions;

public class InvalidTransferIdException extends Exception {
    private int transferIdEntered;

    public InvalidTransferIdException(int transferIdEntered) {
        this.transferIdEntered = transferIdEntered;
    }

    @Override
    public String getMessage(){

        return "Transfer ID " + transferIdEntered + " does not match transfer ID in list.";
    }
}
