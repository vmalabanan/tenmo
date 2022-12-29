package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.pages.DisplayAvatar;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;

public class AccountDetailsGrid {
    private static final int AVATAR_WIDTH = 11;
    private static final String CHAR_TOP_BORDER_OUTER = ".";
    private static final String CHAR_BOTTOM_BORDER_OUTER = "'";
    private static final String CHAR_TOP_BOTTOM_BORDER_INNER = "=";
    private static final String CHAR_SEPARATOR = "-";
    private static final String CHAR_LEFT_RIGHT_BORDER = "|";
    private static final int NUM_SPACES_LEFT_OF_CELL = 5;
    private static final int MIN_SPACES_IN_CELL = 37;
    private static final String OPTION_1 = "1: View your past transfers";
    private static final String OPTION_2 = "2: View your pending requests";
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

    private static int numSpacesBeforeOption1;
    private static int numSpacesAfterOption1;
    private static int numSpacesBeforeOption2;
    private static int numSpacesAfterOption2;
    private static int numSpacesBeforeOption3;
    private static int numSpacesAfterOption3;
    private static int numSpacesBeforeOption4;
    private static int numSpacesAfterOption4;
    private static int numSpacesBeforeOption5;
    private static int numSpacesAfterOption5;
    private static int numSpacesBeforeOption0;
    private static int numSpacesAfterOption0;

    private static String leftSpacing;
    private static String spacingBeforeAvatar;
    private static String spacingAfterAvatar;
    private static String spacingBeforeUsername;
    private static String spacingAfterUsername;
    private static String spacingBeforeBalance;
    private static String spacingAfterBalance;
    private static String spacingInCell;

    private static String spacingBeforeOption1;
    private static String spacingAfterOption1;
    private static String spacingBeforeOption2;
    private static String spacingAfterOption2;
    private static String spacingBeforeOption3;
    private static String spacingAfterOption3;
    private static String spacingBeforeOption4;
    private static String spacingAfterOption4;
    private static String spacingBeforeOption5;
    private static String spacingAfterOption5;
    private static String spacingBeforeOption0;
    private static String spacingAfterOption0;

    public static void printAccountDetails(User user, BigDecimal balance) {
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
        for (int i = 10; i <=16; i++) {
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
            line += spacingBeforeOption1 + OPTION_1 + spacingAfterOption1;
        }

        if (row == 11) {
            line += spacingBeforeOption2 + OPTION_2 + spacingAfterOption2;
        }

        if (row == 11) {
            line += spacingBeforeOption3 + OPTION_3 + spacingAfterOption3;
        }

        if (row == 11) {
            line += spacingBeforeOption4 + OPTION_4 + spacingAfterOption4;
        }

        if (row == 11) {
            line += spacingBeforeOption5 + OPTION_5 + spacingAfterOption5;
        }

        if (row == 11) {
            line += spacingBeforeOption0 + OPTION_0 + spacingAfterOption0;
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
        int balanceLength = "Balance: ".length() + balance.toString().length();
        int usernameLength = user.getUsername().length();

        // number of spaces in cell should be set to the max of the balance string, username, or min spaces in cell
        numOfSpacesInCell = Collections.max(Arrays.asList(balanceLength, usernameLength, MIN_SPACES_IN_CELL));

        // calculate spacing before/after avatar, username, balance, and each of the menu options
        calculateSpacingBeforeAfter(numSpacesBeforeAvatar, numSpacesAfterAvatar, AVATAR_WIDTH);
        calculateSpacingBeforeAfter(numSpacesBeforeUsername, numSpacesAfterUsername, usernameLength);
        calculateSpacingBeforeAfter(numSpacesBeforeBalance, numSpacesAfterBalance, balanceLength);
        calculateSpacingBeforeAfter(numSpacesBeforeOption1, numSpacesAfterOption1, OPTION_1.length());
        calculateSpacingBeforeAfter(numSpacesBeforeOption2, numSpacesAfterOption2, OPTION_2.length());
        calculateSpacingBeforeAfter(numSpacesBeforeOption3, numSpacesAfterOption3, OPTION_3.length());
        calculateSpacingBeforeAfter(numSpacesBeforeOption4, numSpacesAfterOption4, OPTION_4.length());
        calculateSpacingBeforeAfter(numSpacesBeforeOption5, numSpacesAfterOption5, OPTION_5.length());
        calculateSpacingBeforeAfter(numSpacesBeforeOption0, numSpacesAfterOption0, OPTION_0.length());


    }

    private static void calculateSpacingBeforeAfter(int before, int after, int itemLength) {
        before = (numOfSpacesInCell - itemLength) / 2;
        after = numOfSpacesInCell - itemLength - before;
    }

    private static void setSpacing() {

        setSpacing(spacingBeforeAvatar, numSpacesBeforeAvatar);
        setSpacing(spacingAfterAvatar, numSpacesAfterAvatar);
        setSpacing(spacingBeforeUsername, numSpacesBeforeUsername);
        setSpacing(spacingAfterUsername, numSpacesAfterUsername);
        setSpacing(spacingBeforeBalance, numSpacesBeforeBalance);
        setSpacing(spacingAfterBalance, numSpacesAfterBalance);
        setSpacing(leftSpacing, NUM_SPACES_LEFT_OF_CELL);
        setSpacing(spacingInCell, numOfSpacesInCell);

        setSpacing(spacingBeforeOption1, numSpacesBeforeOption1);
        setSpacing(spacingAfterOption1, numSpacesAfterOption1);
        setSpacing(spacingBeforeOption2, numSpacesBeforeOption2);
        setSpacing(spacingAfterOption2, numSpacesAfterOption2);
        setSpacing(spacingBeforeOption3, numSpacesBeforeOption3);
        setSpacing(spacingAfterOption3, numSpacesAfterOption3);
        setSpacing(spacingBeforeOption4, numSpacesBeforeOption4);
        setSpacing(spacingAfterOption4, numSpacesAfterOption4);
        setSpacing(spacingBeforeOption5, numSpacesBeforeOption5);
        setSpacing(spacingAfterOption5, numSpacesAfterOption5);
        setSpacing(spacingBeforeOption0, numSpacesBeforeOption0);
        setSpacing(spacingAfterOption0, numSpacesAfterOption0);

    }

    private static void setSpacing(String string, int numSpaces) {
        String spacing = "";
        for (int i = 0; i < numSpaces; i++) {
            spacing += " ";
        }
        string = spacing;
    }
}
