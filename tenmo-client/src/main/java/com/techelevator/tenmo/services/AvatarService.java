package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AvatarService extends AuthenticatedApiService<Avatar> {
    public List<Avatar> getAllAvatars() {
        List<Avatar> avatars;

        try
        {
            var url = baseUrl + "avatar";
            var entity = makeAuthEntity();
            ResponseEntity<Avatar[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Avatar[].class);
            avatars = Arrays.asList(Objects.requireNonNull(response.getBody()));
        }
        catch(Exception ex)
        {
            avatars = null;
            BasicLogger.log(ex.getMessage());

        }
        return avatars;
    }

    public Avatar changeAvatar(Avatar avatar) {
        Avatar newAvatar;
        try
        {
            var url = baseUrl + "avatar";
            var entity = makeAuthEntity(avatar);
            ResponseEntity<Avatar> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Avatar.class);
            newAvatar = response.getBody();
        }
        catch(Exception ex)
        {
            newAvatar = null;
            BasicLogger.log(ex.getMessage());

        }
        return newAvatar;
    }
}
