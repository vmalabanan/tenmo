package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.views.pages.DisplayAvatar;


public class ChangeAvatarGrid {
    private static final int AVATAR_WIDTH = 11;
    private static final String CHAR_TOP_BORDER_OUTER = ".";
    private static final String CHAR_BOTTOM_BORDER_OUTER = "'";
    private static final String CHAR_TOP_BOTTOM_BORDER_INNER = "=";
    private static final String CHAR_SEPARATOR = "-";
    private static final String CHAR_LEFT_RIGHT_BORDER = "|";
    private static final int NUM_SPACES_LEFT_OF_CELL = 5;
    private static final int MIN_SPACES_IN_CELL = 35;
    private static final String OPTION_1 = "1: Change avatar";
    private static final String OPTION_2 = "2: Change avatar color";
    private static final String OPTION_3 = "3: Change both";
    private static final String OPTION_0 = "0: Cancel";

    private static int numOfSpacesInCell;
    private static int numSpacesBeforeAvatar;
    private static int numSpacesAfterAvatar;
    private static int numSpacesBeforeAvatarDescription;
    private static int numSpacesAfterAvatarDescription;


    private static int numSpacesBeforeOptions;
    private static int numSpacesAfterOption1;
    private static int numSpacesAfterOption2;
    private static int numSpacesAfterOption3;
    private static int numSpacesAfterOption0;

    private static String leftSpacing;
    private static String spacingBeforeAvatar;
    private static String spacingAfterAvatar;
    private static String spacingBeforeAvatarDescription;
    private static String spacingAfterAvatarDescription;
    private static String spacingInCell;

    private static String spacingBeforeOptions;
    private static String spacingAfterOption1;
    private static String spacingAfterOption2;
    private static String spacingAfterOption3;
    private static String spacingAfterOption0;

    public static void printChangeAvatarGrid(Avatar avatar) {
        determineGridSize(avatar);
        setSpacing();

        // line 1
        System.out.println(lineBuilder(CHAR_TOP_BORDER_OUTER));

        // lines 2-7
        for (int i = 2; i <=7; i++) {
            System.out.println(lineBuilder(avatar, i));
        }

        // line 8
        System.out.println(lineBuilder());

        // lines 9-12
        for (int i = 9; i <=12; i++) {
            System.out.println(lineBuilder(avatar, i));
        }

        // line 13
        System.out.println(lineBuilder(CHAR_BOTTOM_BORDER_OUTER));
    }

    // for first and last row (1 and 13)
    private static String lineBuilder(String outerSymbol) {
        String line = leftSpacing;
        line += outerSymbol;
        for (int i = 0; i < numOfSpacesInCell; i++) {
            line += CHAR_TOP_BOTTOM_BORDER_INNER;
        }
        line += outerSymbol;
        return line;
    }

    // for rows 2-7, 9-12
    private static String lineBuilder(Avatar avatar, int row) {
        DisplayAvatar displayAvatar = new DisplayAvatar(avatar);
        String avatarColor = avatar.getColor().getColorDesc();
        String avatarDescr = avatar.getAvatarDesc();

        String line = leftSpacing + CHAR_LEFT_RIGHT_BORDER;

        if (row >= 2 && row <= 6) {
            line += spacingBeforeAvatar + displayAvatar.getLine(row - 1) + spacingAfterAvatar;
        }

        if (row == 7) {
            line += spacingBeforeAvatarDescription + "Current avatar: " +  avatarColor + " " + avatarDescr + spacingAfterAvatarDescription;
        }


        if (row == 9) {
            line += spacingBeforeOptions + OPTION_1 + spacingAfterOption1;
        }

        if (row == 10) {
            line += spacingBeforeOptions + OPTION_2 + spacingAfterOption2;
        }

        if (row == 11) {
            line += spacingBeforeOptions + OPTION_3 + spacingAfterOption3;
        }

        if (row == 12) {
            line += spacingBeforeOptions + OPTION_0 + spacingAfterOption0;
        }

        line += CHAR_LEFT_RIGHT_BORDER;
        return line;

    }

    // for middle separator row (8)
    private static String lineBuilder() {
        String line = leftSpacing;
        line += CHAR_LEFT_RIGHT_BORDER + " ";
        for (int i = 0; i < numOfSpacesInCell - 2; i++) {
            line += CHAR_SEPARATOR;
        }
        line += " " + CHAR_LEFT_RIGHT_BORDER;
        return line;
    }

    private static void determineGridSize(Avatar avatar) {
        String avatarDesc = avatar.getAvatarDesc();
        String avatarColor = avatar.getColor().getColorDesc();

        int currentAvatarLength = "Current avatar:  ".length() + avatarColor.length() + avatarDesc.length();

        // number of spaces in cell should be set to the max of currentAvatarLength or min spaces in cell
        numOfSpacesInCell = Math.max(currentAvatarLength, MIN_SPACES_IN_CELL);

        // calculate spacing before/after avatar, avatar description, and each of the menu options
        numSpacesBeforeAvatar = (numOfSpacesInCell - AVATAR_WIDTH) / 2;
        numSpacesAfterAvatar = numOfSpacesInCell - AVATAR_WIDTH - numSpacesBeforeAvatar;

        numSpacesBeforeAvatarDescription = (numOfSpacesInCell - currentAvatarLength) / 2;
        numSpacesAfterAvatarDescription = numOfSpacesInCell - currentAvatarLength - numSpacesBeforeAvatarDescription;

        // Set menu option spacing according to the longest option (2)
        numSpacesBeforeOptions = (numOfSpacesInCell - OPTION_2.length()) / 2;

        numSpacesAfterOption1 = numOfSpacesInCell - OPTION_1.length() - numSpacesBeforeOptions;

        numSpacesAfterOption2 = numOfSpacesInCell - OPTION_2.length() - numSpacesBeforeOptions;

        numSpacesAfterOption3 = numOfSpacesInCell - OPTION_3.length() - numSpacesBeforeOptions;

        numSpacesAfterOption0 = numOfSpacesInCell - OPTION_0.length() - numSpacesBeforeOptions;

    }

    private static void setSpacing() {
        String spacing = "";
        for (int i = 0; i < numSpacesBeforeAvatar; i++) {
            spacing += " ";
        }
        spacingBeforeAvatar = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterAvatar; i++) {
            spacing += " ";
        }
        spacingAfterAvatar = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesBeforeAvatarDescription; i++) {
            spacing += " ";
        }
        spacingBeforeAvatarDescription = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterAvatarDescription; i++) {
            spacing += " ";
        }
        spacingAfterAvatarDescription = spacing;

        spacing = "";
        for (int i = 0; i < NUM_SPACES_LEFT_OF_CELL; i++) {
            spacing += " ";
        }
        leftSpacing = spacing;

        spacing = "";
        for (int i = 0; i < numOfSpacesInCell; i++) {
            spacing += " ";
        }
        spacingInCell = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesBeforeOptions; i++) {
            spacing += " ";
        }
        spacingBeforeOptions = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterOption1; i++) {
            spacing += " ";
        }
        spacingAfterOption1 = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterOption2; i++) {
            spacing += " ";
        }
        spacingAfterOption2 = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterOption3; i++) {
            spacing += " ";
        }
        spacingAfterOption3 = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterOption0; i++) {
            spacing += " ";
        }
        spacingAfterOption0 = spacing;

    }
}

