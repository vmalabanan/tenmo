package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService extends AuthenticatedApiService<User>
{
    public List<User> getAllUsers()
    {
        List<User> users;

        try
        {
            var url = baseUrl + "users";
            var entity = makeAuthEntity();
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());
        }
        catch(Exception ex)
        {
            users = new ArrayList<>();
            BasicLogger.log(ex.getMessage());
        }

        return users;
    }

}
