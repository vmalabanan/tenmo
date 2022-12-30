package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.pages.DisplayAvatar;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;

public class MainMenuGrid {
    private static final int AVATAR_WIDTH = 11;
    private static final String CHAR_TOP_BORDER_OUTER = ".";
    private static final String CHAR_BOTTOM_BORDER_OUTER = "'";
    private static final String CHAR_TOP_BOTTOM_BORDER_INNER = "=";
    private static final String CHAR_SEPARATOR = "-";
    private static final String CHAR_LEFT_RIGHT_BORDER = "|";
    private static final int NUM_SPACES_LEFT_OF_CELL = 5;
    private static final int MIN_SPACES_IN_CELL = 35;
    private static final String OPTION_1 = "1: View all transfers";
    private static final String OPTION_2 = "2: View pending requests";
    private static final String OPTION_3 = "3: Send TE bucks";
    private static final String OPTION_4 = "4: Request TE bucks";
    private static final String OPTION_5 = "5: Change avatar";
    private static final String OPTION_0 = "0: Exit";

    private static int numOfSpacesInCell;
    private static int numSpacesBeforeAvatar;
    private static int numSpacesAfterAvatar;
    private static int numSpacesBeforeUsername;
    private static int numSpacesAfterUsername;
    private static int numSpacesBeforeBalance;
    private static int numSpacesAfterBalance;

    private static int numSpacesBeforeOptions;
    private static int numSpacesAfterOption1;
    private static int numSpacesAfterOption2;
    private static int numSpacesAfterOption3;
    private static int numSpacesAfterOption4;
    private static int numSpacesAfterOption5;
    private static int numSpacesAfterOption0;

    private static String leftSpacing;
    private static String spacingBeforeAvatar;
    private static String spacingAfterAvatar;
    private static String spacingBeforeUsername;
    private static String spacingAfterUsername;
    private static String spacingBeforeBalance;
    private static String spacingAfterBalance;
    private static String spacingInCell;

    private static String spacingBeforeOptions;
    private static String spacingAfterOption1;
    private static String spacingAfterOption2;
    private static String spacingAfterOption3;
    private static String spacingAfterOption4;
    private static String spacingAfterOption5;
    private static String spacingAfterOption0;

    public static void printMainMenuGrid(User user, BigDecimal balance) {
        determineGridSize(user, balance);
        setSpacing();

        // line 1
        System.out.println(lineBuilder(CHAR_TOP_BORDER_OUTER));

        // lines 2 - 8
        for (int i = 2; i <=8; i++) {
            System.out.println(lineBuilder(user, i));
        }

        // line 9
        System.out.println(lineBuilder(balance));

        // line 10
        System.out.println(lineBuilder());

        // lines 11-16
        for (int i = 11; i <= 16; i++) {
            System.out.println(lineBuilder(user, i));
        }

        // line 17
        System.out.println(lineBuilder(CHAR_BOTTOM_BORDER_OUTER));
    }

    // for first and last row (1 and 17)
    private static String lineBuilder(String outerSymbol) {
        String line = leftSpacing;
        line += outerSymbol;
        for (int i = 0; i < numOfSpacesInCell; i++) {
            line += CHAR_TOP_BOTTOM_BORDER_INNER;
        }
        line += outerSymbol;
        return line;
    }

    // for rows 2-8, 11-16
    private static String lineBuilder(User user, int row) {
        DisplayAvatar displayAvatar = new DisplayAvatar(user.getAvatar());
        String line = leftSpacing + CHAR_LEFT_RIGHT_BORDER;

        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        if (row >= 2 && row <= 6) {
            line += spacingBeforeAvatar + displayAvatar.getLine(row - 1) + spacingAfterAvatar;
        }

        if (row == 7) {
            line += spacingBeforeUsername + user.getUsername() + spacingAfterUsername;
        }

        if (row == 8) {
            line += spacingInCell;
        }

        if (row == 11) {
            line += spacingBeforeOptions + OPTION_1 + spacingAfterOption1;
        }

        if (row == 12) {
            line += spacingBeforeOptions + OPTION_2 + spacingAfterOption2;
        }

        if (row == 13) {
            line += spacingBeforeOptions + OPTION_3 + spacingAfterOption3;
        }

        if (row == 14) {
            line += spacingBeforeOptions + OPTION_4 + spacingAfterOption4;
        }

        if (row == 15) {
            line += spacingBeforeOptions + OPTION_5 + spacingAfterOption5;
        }

        if (row == 16) {
            line += spacingBeforeOptions + OPTION_0 + spacingAfterOption0;
        }

        line += CHAR_LEFT_RIGHT_BORDER;
        return line;

    }

    // for row 9
    private static String lineBuilder(BigDecimal balance) {
        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        String line = leftSpacing + CHAR_LEFT_RIGHT_BORDER + spacingBeforeBalance;
        line += "Balance: " + n.format(balance) + spacingAfterBalance + CHAR_LEFT_RIGHT_BORDER;

        return line;
    }

    // for middle separator row (10)
    private static String lineBuilder() {
        String line = leftSpacing;
        line += CHAR_LEFT_RIGHT_BORDER + " ";
        for (int i = 0; i < numOfSpacesInCell - 2; i++) {
            line += CHAR_SEPARATOR;
        }
        line += " " + CHAR_LEFT_RIGHT_BORDER;
        return line;
    }

    private static void determineGridSize(User user, BigDecimal balance) {
        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        int balanceLength = "Balance: ".length() + n.format(balance).length();
        int usernameLength = user.getUsername().length();

        // number of spaces in cell should be set to the max of the balance string, username, or min spaces in cell
        numOfSpacesInCell = Collections.max(Arrays.asList(balanceLength, usernameLength, MIN_SPACES_IN_CELL));

        // calculate spacing before/after avatar, username, balance, and each of the menu options
        numSpacesBeforeAvatar = (numOfSpacesInCell - AVATAR_WIDTH) / 2;
        numSpacesAfterAvatar = numOfSpacesInCell - AVATAR_WIDTH - numSpacesBeforeAvatar;

        numSpacesBeforeUsername = (numOfSpacesInCell - usernameLength) / 2;
        numSpacesAfterUsername = numOfSpacesInCell - usernameLength - numSpacesBeforeUsername;

        numSpacesBeforeBalance = (numOfSpacesInCell - balanceLength) / 2;
        numSpacesAfterBalance = numOfSpacesInCell - balanceLength - numSpacesBeforeBalance;

        // Set menu option spacing according to the longest option (2)
        numSpacesBeforeOptions = (numOfSpacesInCell - OPTION_2.length()) / 2;

        numSpacesAfterOption1 = numOfSpacesInCell - OPTION_1.length() - numSpacesBeforeOptions;

        numSpacesAfterOption2 = numOfSpacesInCell - OPTION_2.length() - numSpacesBeforeOptions;

        numSpacesAfterOption3 = numOfSpacesInCell - OPTION_3.length() - numSpacesBeforeOptions;

        numSpacesAfterOption4 = numOfSpacesInCell - OPTION_4.length() - numSpacesBeforeOptions;

        numSpacesAfterOption5 = numOfSpacesInCell - OPTION_5.length() - numSpacesBeforeOptions;

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
        for (int i = 0; i < numSpacesBeforeUsername; i++) {
            spacing += " ";
        }
        spacingBeforeUsername = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterUsername; i++) {
            spacing += " ";
        }
        spacingAfterUsername = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesBeforeBalance; i++) {
            spacing += " ";
        }
        spacingBeforeBalance = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterBalance; i++) {
            spacing += " ";
        }
        spacingAfterBalance = spacing;

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
        for (int i = 0; i < numSpacesAfterOption4; i++) {
            spacing += " ";
        }
        spacingAfterOption4 = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterOption5; i++) {
            spacing += " ";
        }
        spacingAfterOption5 = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterOption0; i++) {
            spacing += " ";
        }
        spacingAfterOption0 = spacing;

    }
}
