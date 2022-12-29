package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.views.constants.ColorCodes;

import java.util.List;

public class ChangeAvatarColorPage extends BasePage {
    private static final String COLOR_RESET = ColorCodes.RESET;

    public static void displayColors(List<Color> colors) {
        printHeader("Colors");

        String line = "";
        for (int i = 0; i < colors.size(); i++) {
            String color = colors.get(i).getColorDesc();
            String colorCode = mapColorDescriptionToColorCode(color);
            // number each color, add colorCode, and color name
            line += "(" + (i + 1) + ") " + colorCode + color + COLOR_RESET + "\n";
        }

        print(line);
    }

    public static Color makeColorSelection(List<Color> colors, int choice) {
        return colors.get(choice - 1);
    }

    private static String mapColorDescriptionToColorCode(String colorDesc) {
        String color = colorDesc.toLowerCase();

        switch (color) {
            case "red":
                return ColorCodes.RED;
            case "green":
                return ColorCodes.GREEN;
            case "yellow":
                return ColorCodes.YELLOW;
            case "blue":
                return ColorCodes.BLUE;
            case "purple":
                return ColorCodes.PURPLE;
            case "cyan":
                return ColorCodes.CYAN;
            case "white":
                return ColorCodes.WHITE;
        }
        return "";
    }

}
