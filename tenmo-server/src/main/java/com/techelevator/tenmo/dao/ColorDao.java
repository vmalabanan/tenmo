package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.models.User;

import java.util.List;

public interface ColorDao {
    Color changeColor(Color color, int id);

    List<Color> getAll();

    List<Color> getApplicableColors(User user);
}
