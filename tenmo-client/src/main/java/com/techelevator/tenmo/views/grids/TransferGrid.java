package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.constants.ColorCodes;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;

public class TransferGrid {
    private static final int AVATAR_WIDTH = 11;
    private static final String CHAR_TOP_BORDER_OUTER = ".";
    private static final String CHAR_BOTTOM_BORDER_OUTER = "'";
    private static final String CHAR_TOP_BOTTOM_BORDER_INNER = "=";
    private static final String CHAR_LEFT_RIGHT_BORDER = "|";
    private static final int NUM_SPACES_AFTER_ID = 1;
    private static final int NUM_SPACES_BEFORE_AVATAR = 1;
    private static final int NUM_SPACES_AFTER_AVATAR_BEFORE_USERNAME = 3;
    private static final int NUM_SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT = 15;
    private static final int NUM_SPACES_AFTER_AMOUNT = 3;

    private static int numSpacesAfterAvatar;
    private static int numSpacesLeftOfCell;
    private static int numSpacesInCell;
    private static int lengthLongestUsername;
    private static String strLeftSpacing;
    private static String strSpacingBeforeAvatar;
    private static String strSpacingAfterAvatar;
    private static String strSpacingAfterAvatarBeforeUsername;
    private static String strSpacingAfterAmount;


    public static void printTransferGrid(List<Transfer> transfers, int id) {
        determineGridSize(transfers, id);
        setSpacing();

        for (Transfer transfer : transfers) {
            printRow(transfer, id);
        }
    }

    private static void printRow(Transfer transfer, int id) {
        // line 1
        System.out.println(lineBuilder(CHAR_TOP_BORDER_OUTER));

        // line 2-6
        for (int i = 2; i <= 6; i++) {
            System.out.println(lineBuilder(transfer, id, i));
        }

        // line 7
        System.out.println(lineBuilder(CHAR_BOTTOM_BORDER_OUTER));

    }

    // for first and last row (1 and 7) of each item
    private static String lineBuilder(String edgeSymbol) {
        String line = strLeftSpacing;
        line += edgeSymbol;
        for (int i = 0; i < numSpacesInCell; i++) {
            line += CHAR_TOP_BOTTOM_BORDER_INNER;
        }
        line += edgeSymbol;
        return line;
    }

    // for rows 2-6 of each item
    private static String lineBuilder(Transfer transfer, int id, int row) {
        // If transfer is FROM current user, we want to display userTo
        // If transfer is TO current user, we want to display userFrom
        User userToDisplay = getUser(transfer, id);

        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        String line = strLeftSpacing + CHAR_LEFT_RIGHT_BORDER + strSpacingBeforeAvatar;

        switch (row) {
            case 2:
                line += userToDisplay.getAvatar().getAvatarLine1() + strSpacingAfterAvatar;
                break;
            case 3:
                line += userToDisplay.getAvatar().getAvatarLine2() + strSpacingAfterAvatarBeforeUsername;

                // format username to have a width = longestUsernameLength, justified left
                line += String.format("%-"+ lengthLongestUsername +"s", userToDisplay.getUsername());

                // if transfer is FROM current user
                if (isTransferFromCurrentUser(transfer, id)) {
                    // add red font and a leading - sign for amount
                    // format amount to have a width = SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT, justified right
                    line += ColorCodes.RED + String.format("%" + NUM_SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT + "s", "-" + n.format(transfer.getAmount())) + ColorCodes.RESET;
                }
                else {
                    line += String.format("%" + NUM_SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT + "s", n.format(transfer.getAmount()));
                }

                line += strSpacingAfterAmount;

                break;
            case 4:
                line = ""; // line 4 is a special case; reset line's value
                // format transfer id to have a width = numSpacesToLeftOfCell, justified left
                line += String.format("%-"+ numSpacesLeftOfCell +"d", transfer.getTransferId());
                line += CHAR_LEFT_RIGHT_BORDER + strSpacingBeforeAvatar + userToDisplay.getAvatar().getAvatarLine3() + strSpacingAfterAvatarBeforeUsername;
                // format date to have a width = longest username, justified left
                line += String.format("%-"+ lengthLongestUsername +"s", getFormattedDate(transfer));
                String status = transfer.getTransferStatusDesc();
                // transfer status should be color coded according to rejected, approved, pending
                if (status.equalsIgnoreCase("Approved")) {line += ColorCodes.BLUE;}
                else if (status.equalsIgnoreCase("Pending")) {line += ColorCodes.YELLOW;}
                else {line += ColorCodes.PURPLE;}

                // format transfer status to have a width = number of spaces after longest username to the end of amount, justified right
                line += String.format("%"+ (NUM_SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT) +"s", status) + strSpacingAfterAmount + ColorCodes.RESET;
                break;
            case 5:
                line += userToDisplay.getAvatar().getAvatarLine4() + strSpacingAfterAvatar;
                break;
            case 6:
                line += userToDisplay.getAvatar().getAvatarLine5() + strSpacingAfterAvatar;
                break;
        }

        line += CHAR_LEFT_RIGHT_BORDER;
        return line;
    }


    private static User getUser(Transfer transfer, int id) {
        // if transfer is FROM current user,
        // set user = userTo
        if (isTransferFromCurrentUser(transfer, id)) {
            return transfer.getUserTo();
        }
        // otherwise (i.e., if transfer is TO current user),
        // set user = userFrom
        else {
            return transfer.getUserFrom();
        }
    }

    private static boolean isTransferFromCurrentUser(Transfer transfer, int id) {
        return transfer.getUserFrom().getId() == id;
    }

    private static void determineGridSize(List<Transfer> transfers, int id) {
        String longestId = "";
        String longestUsername = "";
        String longestAmount = "";

        for (Transfer transfer : transfers) {
            String idAsString = String.valueOf(transfer.getTransferId());
            String amount = transfer.getAmount().toString();
            String username;
            if (idAsString.length() > longestId.length()) {
                longestId = idAsString;
            }
            if (amount.length() > longestAmount.length()) {
                longestAmount = amount;
            }

            // if transfer is FROM current user,
            // we want to display usernameTo
            // and format the amount as a negative
            if (isTransferFromCurrentUser(transfer, id)) {
                username = transfer.getUserTo().getUsername();
                longestAmount += "-"; // just for character count
             }
            // otherwise (i.e., if transfer is TO current user),
            // we want to display usernameFrom
            else {
                username = transfer.getUserFrom().getUsername();
            }
            if (username.length() > longestUsername.length()) {
                longestUsername = username;
            }
        numSpacesLeftOfCell = longestId.length() + NUM_SPACES_AFTER_ID;
        lengthLongestUsername = longestUsername.length();
        numSpacesInCell = NUM_SPACES_BEFORE_AVATAR + AVATAR_WIDTH + NUM_SPACES_AFTER_AVATAR_BEFORE_USERNAME + lengthLongestUsername + NUM_SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT + NUM_SPACES_AFTER_AMOUNT;
        numSpacesAfterAvatar = NUM_SPACES_AFTER_AVATAR_BEFORE_USERNAME + lengthLongestUsername + NUM_SPACES_AFTER_LONGEST_USERNAME_TO_END_OF_AMOUNT + NUM_SPACES_AFTER_AMOUNT;
        }
    }



    private static void setSpacing() {
        String spacing = "";
        for (int i = 0; i < numSpacesLeftOfCell; i++) {
            spacing += " ";
        }
        strLeftSpacing = spacing;

        spacing = "";
        for (int i = 0; i < NUM_SPACES_BEFORE_AVATAR; i++) {
            spacing += " ";
        }

        strSpacingBeforeAvatar = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterAvatar; i++) {
            spacing += " ";
        }
        strSpacingAfterAvatar = spacing;

        spacing = "";
        for (int i = 0; i < NUM_SPACES_AFTER_AVATAR_BEFORE_USERNAME; i++) {
            spacing += " ";
        }
        strSpacingAfterAvatarBeforeUsername = spacing;

        spacing = "";
        for (int i = 0; i < NUM_SPACES_AFTER_AMOUNT; i++) {
            spacing += " ";
        }
        strSpacingAfterAmount = spacing;

    }


    private static String getFormattedDate(Transfer transfer) {
        // An array of short month names (e.g., Jan, Feb, etc).
        String[] months = new DateFormatSymbols().getShortMonths();
        // Parse the string dateTime into a LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(transfer.getTransferDateTime());
        int monthInt = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();

        return months[monthInt - 1] + " " + day; // - 1 is because months starts at index 0
    }


}


