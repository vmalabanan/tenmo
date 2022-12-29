package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.Avatar;

import java.util.List;

public class AvatarGrid {
    private static String H_SPACING_BETWEEN_AVATARS = "\t\t";
    private static String NUMBER_SPACING = "    ";
    private static String V_SPACING_BETWEEN_AVATARS = "\n\n";

    public static void printAvatarGrid(List<Avatar> avatars) {
        String line = "";
        // display avatars in a grid of width = 3
        for (int i = 0; i < avatars.size(); i+=3) {
            // display name of each avatar
            line += formatAvatarName(avatars.get(i).getAvatarDesc()) + H_SPACING_BETWEEN_AVATARS;
            if (i + 1 < avatars.size()) {
                line += formatAvatarName(avatars.get(i + 1).getAvatarDesc()) + H_SPACING_BETWEEN_AVATARS;
            }
            if (i + 2 < avatars.size()) {
                line += formatAvatarName(avatars.get(i + 2).getAvatarDesc()) + H_SPACING_BETWEEN_AVATARS;
            }
            line += "\n";

            // display all 5 lines of each avatar
            for (int j = 0; j < 5; j++) {
                line += getAvatarByLine(avatars.get(i), j + 1) + H_SPACING_BETWEEN_AVATARS;
                if (i + 1 < avatars.size()) {
                    line += getAvatarByLine(avatars.get(i + 1), j + 1) + H_SPACING_BETWEEN_AVATARS;
                }
                if (i + 2 < avatars.size()) {
                    line += getAvatarByLine(avatars.get(i + 2), j + 1) + H_SPACING_BETWEEN_AVATARS;
                }
                line += "\n"; // start each avatar line on a new line
            }

            // number each avatar starting from 1
            line += NUMBER_SPACING + "(" + (i + 1) + ")" + NUMBER_SPACING + H_SPACING_BETWEEN_AVATARS;
            if (i + 1 < avatars.size()) {
                line += NUMBER_SPACING + "(" + (i + 2) + ")" + NUMBER_SPACING + H_SPACING_BETWEEN_AVATARS;
            }
            if (i + 2 < avatars.size()) {
                line += NUMBER_SPACING + "(" + (i + 3) + ")" + NUMBER_SPACING + H_SPACING_BETWEEN_AVATARS;
            }

            line += V_SPACING_BETWEEN_AVATARS; // start each new set of avatars on a new line
        }

        System.out.println(line);
    }

    private static String getAvatarByLine(Avatar avatar, int line) {
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

    private static String formatAvatarName(String name) {
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
