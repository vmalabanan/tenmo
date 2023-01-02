package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AvatarDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/avatar")
@PreAuthorize("isAuthenticated()")
public class AvatarController {
    private AvatarDao avatarDao;
    private UserDao userDao;

    @Autowired
    public AvatarController(AvatarDao avatarDao, UserDao userDao) {
        this.avatarDao = avatarDao;
        this.userDao = userDao;
    }

    @GetMapping
    public List<Avatar> getAllAvatars(Principal principal) {
        int id = userDao.getIdByUsername(principal.getName());
        User user = userDao.getUserById(id);

        return avatarDao.getApplicableAvatars(user);
    }


    @PutMapping
    public Avatar changeAvatar(@RequestBody Avatar avatar, Principal principal) {
        int id = userDao.getIdByUsername(principal.getName());

        return avatarDao.changeAvatar(avatar, id);
    }
}
