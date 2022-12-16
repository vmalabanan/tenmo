package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.User;

import java.util.List;

public class UserListPage extends BasePage {

    public void displayUsers(List<User> users){
        printHeader("Users");

        users.forEach(user -> {
            print(user.getUsername());
        });
    }
}

