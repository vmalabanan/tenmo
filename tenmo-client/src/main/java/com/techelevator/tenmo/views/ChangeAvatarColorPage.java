package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.Color;

import java.util.List;

public class ChangeAvatarColorPage extends DisplayAvatar {
    public ChangeAvatarColorPage(Avatar avatar) {
        super(avatar);
    }

    public void displayColors(List<Color> colors) {
        printHeader("Colors");

        String line = "";
        for (int i = 0; i < colors.size(); i++) {
            String color = colors.get(i).getColorDesc();
            String colorCode = mapColorDescriptionToColorCode(color);
            // number each color, add colorCode, and color name
            line += "(" + (i + 1) + ") " + colorCode + color + getCOLOR_RESET() + "\n";
        }

        print(line);
    }

    public Color makeColorSelection(List<Color> colors, int choice) {
        return colors.get(choice - 1);
    }


}
