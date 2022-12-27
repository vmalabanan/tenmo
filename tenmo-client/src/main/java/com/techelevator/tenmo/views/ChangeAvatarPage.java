package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.Color;

import java.util.List;

public class ChangeAvatarPage extends DisplayAvatar {

    public ChangeAvatarPage(Avatar avatar) {
        super(avatar);
    }

    public void displayCurrentAvatar() {
        printLine("Current avatar:");
        printAvatar(super.getAvatar());
        printLine();
    }

    public void changeAvatarOptions() {
        printLine("What do you want to do?");
        printLine("1: Change avatar");
        printLine("2: Change avatar color");
        printLine("3: Change both");
        printLine("0: Cancel");
    }

    public int getChangeAvatarSelection() {
        changeAvatarOptions();
        return getSelection(0, 3);
    }

    public Avatar makeAvatarSelection(List<Avatar> avatars, int choice) {
        return avatars.get(choice - 1);
    }

    public int getColorSelection(int numOfColors) {
        return getSelection(0, numOfColors);
    }

    public int getSelection(int minMenuOption, int maxMenuOption) {
        int id = getIntValue("Please make a selection (0) to cancel: ");

        // if selection isn't valid
        if (id < minMenuOption || id > maxMenuOption) {
            print("Invalid selection");
            return getSelection(minMenuOption, maxMenuOption);
        }

        return id;
    }


    public void displayAvatars(List<Avatar> avatars) {
        printHeader("Avatars");
        String line = "";
        String hSpacingBetweenAvatars = "\t\t";
        String numberSpacing = "    ";
        String vSpacingBetweenAvatars = "\n\n";

        // display avatars in a grid of width = 3
        for (int i = 0; i < avatars.size(); i+=3) {
            // display name of each avatar
            line += formatAvatarName(avatars.get(i).getAvatarDesc()) + hSpacingBetweenAvatars;
            if (i + 1 < avatars.size()) {
                line += formatAvatarName(avatars.get(i + 1).getAvatarDesc()) + hSpacingBetweenAvatars;
            }
            if (i + 2 < avatars.size()) {
                line += formatAvatarName(avatars.get(i + 2).getAvatarDesc()) + hSpacingBetweenAvatars;
            }
            line += "\n";

            // display all 5 lines of each avatar
            for (int j = 0; j < 5; j++) {
                line += getAvatarByLine(avatars.get(i), j + 1) + hSpacingBetweenAvatars;
                if (i + 1 < avatars.size()) {
                    line += getAvatarByLine(avatars.get(i + 1), j + 1) + hSpacingBetweenAvatars;
                }
                if (i + 2 < avatars.size()) {
                    line += getAvatarByLine(avatars.get(i + 2), j + 1) + hSpacingBetweenAvatars;
                }
                line += "\n"; // start each avatar line on a new line
            }

            // number each avatar starting from 1
            line += numberSpacing + "(" + (i + 1) + ")" + numberSpacing + hSpacingBetweenAvatars;
            if (i + 1 < avatars.size()) {
                line += numberSpacing + "(" + (i + 2) + ")" + numberSpacing + hSpacingBetweenAvatars;
            }
            if (i + 2 < avatars.size()) {
                line += numberSpacing + "(" + (i + 3) + ")" + numberSpacing + hSpacingBetweenAvatars;
            }

            line += vSpacingBetweenAvatars; // start each new set of avatars on a new line
        }

        print(line);
    }

    public void displayColors(List<Color> colors) {
        for (int i = 0; i < colors.size(); i++) {
            String color = colors.get(i).getColorDesc();
            String colorCode = mapColorDescriptionToColorCode(color);
            // number each color starting from 1
            printLine("(" + (i + 1) + ") " + colorCode + color + getCOLOR_RESET());

        }
    }

    private String formatAvatarName(String name) {
        int avatarWidth = 11;
        int numLeftSpaces = (avatarWidth - name.length()) / 2;
        int numRightSpaces = avatarWidth -  name.length() - numLeftSpaces;
        String leftSpacing = "";
        String rightSpacing = "";

        for (int i = 0; i < numLeftSpaces; i++) {
            leftSpacing += " ";
        }

        for (int i = 0; i < numRightSpaces; i++) {
            rightSpacing += " ";
        }
        return leftSpacing + name + rightSpacing;
    }
}
