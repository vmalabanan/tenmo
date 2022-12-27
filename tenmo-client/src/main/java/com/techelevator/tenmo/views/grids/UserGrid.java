package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.User;

import java.util.List;

public class UserGrid {
    private static final int AVATAR_WIDTH = 11;
    private static final String CHAR_TOP_BORDER_OUTER = ".";
    private static final String CHAR_BOTTOM_BORDER_OUTER = "'";
    private static final String CHAR_TOP_BOTTOM_BORDER_INNER = "=";
    private static final String CHAR_LEFT_RIGHT_BORDER = "|";
    private static final int SPACES_AFTER_ID = 1;
    private static final int SPACES_BEFORE_AVATAR = 1;
    private static final int SPACES_AFTER_AVATAR_BEFORE_USERNAME = 2;
    private static final int SPACES_AFTER_LONGEST_USERNAME = 2;

    private static int numOfSpacesToLeftOfCell;
    private static int numOfSpacesInCell;
    private static int longestUsernameLength;
    private static String leftSpacing;
    private static String spacingBeforeAvatar;
    private static String spacingAfterAvatar;
    private static String spacingAfterAvatarBeforeUsername;
    private static String spacingAfterLongestUsername;

    public static void printUserGrid(List<User> users) {
        determineGridSize(users);
        setSpacing();

        for (User user : users) {
            printRow(user);
        }
    }

    private static void printRow(User user) {
        // line 1
        System.out.println(lineBuilder(CHAR_TOP_BORDER_OUTER));

        // line 2
        System.out.println(lineBuilder(user, 2));

        // line 3
        System.out.println(lineBuilder(user, 3));

        // line 4
        System.out.println(lineBuilder(user, 4));

        // line 5
        System.out.println(lineBuilder(user, 5));

        // line 6
        System.out.println(lineBuilder(user, 6));

        // line 7
        System.out.println(lineBuilder(CHAR_BOTTOM_BORDER_OUTER));

    }

    // for first and last row (1 and 7) of each item
    private static String lineBuilder(String edgeSymbol) {
        String line = leftSpacing;
        line += edgeSymbol;
        for (int i = 0; i < numOfSpacesInCell; i++) {
            line += CHAR_TOP_BOTTOM_BORDER_INNER;
        }
        line += edgeSymbol;
        return line;
    }

    // for rows 2-6 of each item
    private static String lineBuilder(User user, int row) {
        String line = leftSpacing + CHAR_LEFT_RIGHT_BORDER + spacingBeforeAvatar;
        switch (row) {
            case 2:
                line += user.getAvatar().getAvatarLine1() + spacingAfterAvatar;
                break;
            case 3:
                line += user.getAvatar().getAvatarLine2() + spacingAfterAvatar;
                break;
            case 4:
                line = ""; // line 4 is a special case; reset line's value
                // format id to have a width = numOfSpacesToLeftOfCell, justified left
                line += String.format("%-"+ numOfSpacesToLeftOfCell +"d", user.getId());
                line += CHAR_LEFT_RIGHT_BORDER + spacingBeforeAvatar + user.getAvatar().getAvatarLine3() + spacingAfterAvatarBeforeUsername;
                // format usernameTo to have a width = longestUsernameLength + spacing, justified left
                line += String.format("%-"+ (longestUsernameLength + SPACES_AFTER_LONGEST_USERNAME) +"s", user.getUsername());
                break;
            case 5:
                line += user.getAvatar().getAvatarLine4() + spacingAfterAvatar;
                break;
            case 6:
                line += user.getAvatar().getAvatarLine5() + spacingAfterAvatar;
                break;
        }

        line += CHAR_LEFT_RIGHT_BORDER;
        return line;
    }

    private static void determineGridSize(List<User> users) {
        String longestId = "";
        String longestUsername = "";
        for (User user : users) {
            String idAsString = String.valueOf(user.getId());
            if (idAsString.length() > longestId.length()) {
                longestId = idAsString;
            }
            if (user.getUsername().length() > longestUsername.length()) {
                longestUsername = user.getUsername();
            }
        }
        numOfSpacesToLeftOfCell = longestId.length() + SPACES_AFTER_ID;
        longestUsernameLength = longestUsername.length();
        numOfSpacesInCell = SPACES_BEFORE_AVATAR + AVATAR_WIDTH + SPACES_AFTER_AVATAR_BEFORE_USERNAME + longestUsernameLength + SPACES_AFTER_LONGEST_USERNAME;
    }

    private static void setSpacing() {
        String spacing = "";
        for (int i = 0; i < numOfSpacesToLeftOfCell; i++) {
            spacing += " ";
        }
        leftSpacing = spacing;

        spacing = "";
        for (int i = 0; i < SPACES_BEFORE_AVATAR; i++) {
            spacing += " ";
        }

        spacingBeforeAvatar = spacing;

        spacing = "";
        for (int i = 0; i < SPACES_AFTER_AVATAR_BEFORE_USERNAME + longestUsernameLength + SPACES_AFTER_LONGEST_USERNAME; i++) {
            spacing += " ";
        }
        spacingAfterAvatar = spacing;

        spacing = "";
        for (int i = 0; i < SPACES_AFTER_AVATAR_BEFORE_USERNAME; i++) {
            spacing += " ";
        }
        spacingAfterAvatarBeforeUsername = spacing;

        spacing = "";
        for (int i = 0; i < SPACES_AFTER_LONGEST_USERNAME; i++) {
            spacing += " ";
        }
        spacingAfterLongestUsername = spacing;
    }



}


