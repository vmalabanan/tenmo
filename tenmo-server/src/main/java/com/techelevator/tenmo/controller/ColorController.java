package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.ColorDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/color")
@PreAuthorize("isAuthenticated()")
public class ColorController {
    private ColorDao colorDao;
    private UserDao userDao;

    @Autowired
    public ColorController(ColorDao colorDao, UserDao userDao) {
        this.colorDao = colorDao;
        this.userDao = userDao;
    }

    @GetMapping
    public List<Color> getAllColors(Principal principal) {
        int id = userDao.getIdByUsername(principal.getName());
        User user = userDao.getUserById(id);

        return colorDao.getApplicableColors(user);
    }

    @PutMapping
    public Color changeColor(@RequestBody Color color, Principal principal) {
        int id = userDao.getIdByUsername(principal.getName());

        return colorDao.changeColor(color, id);
    }
}
