package com.techelevator.tenmo.models.exceptions;

public class InvalidUserIdException extends Exception {
    private int userIdEntered;

    public InvalidUserIdException(int userIdEntered) {
        this.userIdEntered = userIdEntered;
    }

    @Override
    public String getMessage(){
        return "User ID " + userIdEntered + " does not match a user ID in list.";
    }

}
