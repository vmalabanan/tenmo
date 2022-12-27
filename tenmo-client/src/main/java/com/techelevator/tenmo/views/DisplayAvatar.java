package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.views.constants.ColorCodes;

public abstract class DisplayAvatar extends BasePage {
    private Avatar avatar;
    private String colorCode;
    private final String COLOR_RESET = ColorCodes.RESET;

    public DisplayAvatar(Avatar avatar) {
        this.avatar = avatar;
        this.colorCode = mapColorDescriptionToColorCode(avatar);
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getCOLOR_RESET() {
        return COLOR_RESET;
    }

    public void printAvatar(Avatar avatar) {

        printLine(colorCode + avatar.getAvatarLine1());
        printLine(avatar.getAvatarLine2());
        printLine(avatar.getAvatarLine3());
        printLine(avatar.getAvatarLine4());
        printLine(avatar.getAvatarLine5() + COLOR_RESET);

    }

    public String getAvatarByLine(int line) {
        String str = "";

        switch (line) {
            case 1:
                str += avatar.getAvatarLine1();
                break;
            case 2:
                str += avatar.getAvatarLine2();
                break;
            case 3:
                str += avatar.getAvatarLine3();
                break;
            case 4:
                str += avatar.getAvatarLine4();
                break;
            case 5:
                str += avatar.getAvatarLine5();
                break;
        }

        return str;
    }

    public String getAvatarByLine(Avatar avatar, int line) {
        String str = "";

        switch (line) {
            case 1:
                str += avatar.getAvatarLine1();
                break;
            case 2:
                str += avatar.getAvatarLine2();
                break;
            case 3:
                str += avatar.getAvatarLine3();
                break;
            case 4:
                str += avatar.getAvatarLine4();
                break;
            case 5:
                str += avatar.getAvatarLine5();
                break;
        }

        return str;
    }


    public String mapColorDescriptionToColorCode(Avatar avatar) {
        String color = avatar.getColor().getColorDesc().toLowerCase();

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

    public String mapColorDescriptionToColorCode(String colorDesc) {
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
