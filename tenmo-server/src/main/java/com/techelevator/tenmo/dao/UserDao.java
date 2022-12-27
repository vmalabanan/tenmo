package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.models.User;

import java.util.List;

public interface UserDao
{
    List<User> getAll();

    List<User> getAllExceptCurrent(int id);

    User getUserById(int id);

    User getByUsername(String username);

    int getIdByUsername(String username);

    boolean create(String username, String password);
}
