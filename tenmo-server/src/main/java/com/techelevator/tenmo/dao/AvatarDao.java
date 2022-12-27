package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.User;

import java.util.List;

public interface AvatarDao {
    Avatar changeAvatar(Avatar avatar, int id);

    List<Avatar> getAll();

    List<Avatar> getApplicableAvatars(User user);
}
