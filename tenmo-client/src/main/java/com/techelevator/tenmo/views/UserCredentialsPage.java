package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.UserCredentials;

public class UserCredentialsPage extends BasePage {
    public UserCredentials getUserCredential(String pageName)
    {
        printHeader(pageName);

        var username = getValue("Enter a username: ");
        var password = getValue("Enter a password: ");

        // using the constructor parameters
        var user = new UserCredentials(username, password);

        return user;
    }
}
