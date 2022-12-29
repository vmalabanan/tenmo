package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.views.pages.BasePage;

public class UserCredentialsPage extends BasePage {
    public static UserCredentials getUserCredential(String pageName)
    {
        printHeader(pageName);

        var username = getValue("Enter a username: ").toLowerCase(); // usernames are stored as lowercase only
        var password = getValue("Enter a password: ");

        // using the constructor parameters
        var user = new UserCredentials(username, password);

        return user;
    }
}
