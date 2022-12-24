package com.techelevator.tenmo.views.grids;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.constants.ColorCodes;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransferDetails {

    private static final int AVATAR_WIDTH = 11;
    private static final String CHAR_TOP_BORDER_OUTER = ".";
    private static final String CHAR_BOTTOM_BORDER_OUTER = "'";
    private static final String CHAR_TOP_BOTTOM_BORDER_INNER = "=";
    private static final String CHAR_LEFT_RIGHT_BORDER = "|";
    private static final int NUM_SPACES_LEFT_OF_CELL = 5;
    private static final int NUM_SPACES_PADDING_LEFT_RIGHT = 2;
    private static final int MAX_LENGTH_MESSAGE = 35;

    private static String formattedDate;

    private static int numSpacesInCell;
    private static int lengthUsernameFrom;
    private static int lengthUsernameTo;
    private static int numSpacesBetweenAvatars;
    private static int numSpacesBetweenUsernames;
    private static int numSpacesAfterMessage;
    private static int numSpacesAfterAmount;
    private static int numSpacesAfterStatus;
    private static int numSpacesAfterType;
    private static int numSpacesAfterDate;

    private static String strLeftSpacing;
    private static String strSpacingInCell;
    private static String strSpacingPaddingLeftRight;
    private static String strSpacingBetweenAvatars;
    private static String strSpacingBetweenUsernames;
    private static String strSpacingAfterMessage;
    private static String strSpacingAfterAmount;
    private static String strSpacingAfterStatus;
    private static String strSpacingAfterType;
    private static String strSpacingAfterDate;


    public static void printTransferDetails(Transfer transfer, int id) {
        determineGridSize(transfer, id);
        setSpacing();
        printRow(transfer, id);
    }

    private static void printRow(Transfer transfer, int id) {
        // line 1
        System.out.println(lineBuilder(CHAR_TOP_BORDER_OUTER));

        // line 2-13
        for (int i = 2; i <= 13; i++) {
            System.out.println(lineBuilder(transfer, id, i));
        }

        // line 14
        System.out.println(lineBuilder(CHAR_BOTTOM_BORDER_OUTER));

    }

    // for first and last row (1 and 14)
    private static String lineBuilder(String edgeSymbol) {
        String line = strLeftSpacing;
        line += edgeSymbol;
        for (int i = 0; i < numSpacesInCell; i++) {
            line += CHAR_TOP_BOTTOM_BORDER_INNER;
        }
        line += edgeSymbol;
        return line;
    }

    // for rows 2-13
    private static String lineBuilder(Transfer transfer, int id, int row) {
        User userFr = transfer.getUserFrom();
        User userTo = transfer.getUserTo();
        // to format amount as money
        NumberFormat n = NumberFormat.getCurrencyInstance();

        String line = strLeftSpacing + CHAR_LEFT_RIGHT_BORDER + strSpacingPaddingLeftRight;

        switch (row) {
            case 2:
                line += userFr.getAvatar().getAvatarLine1() + strSpacingBetweenAvatars;
                line += userTo.getAvatar().getAvatarLine1() + strSpacingPaddingLeftRight;
                break;
            case 3:
                line += userFr.getAvatar().getAvatarLine2() + strSpacingBetweenAvatars;
                line += userTo.getAvatar().getAvatarLine2() + strSpacingPaddingLeftRight;
                break;
            case 4:
                line += userFr.getAvatar().getAvatarLine3() + strSpacingBetweenAvatars;
                line += userTo.getAvatar().getAvatarLine3() + strSpacingPaddingLeftRight;
                break;
            case 5:
                line += userFr.getAvatar().getAvatarLine4() + strSpacingBetweenAvatars;
                line += userTo.getAvatar().getAvatarLine4() + strSpacingPaddingLeftRight;
                break;
            case 6:
                line += userFr.getAvatar().getAvatarLine5() + strSpacingBetweenAvatars;
                line += userTo.getAvatar().getAvatarLine5() + strSpacingPaddingLeftRight;
                break;
            case 7:
                line += "From: " + userFr.getUsername() + strSpacingBetweenUsernames;
                line += "To: " + userTo.getUsername() + strSpacingPaddingLeftRight;
                break;
            case 8: // 8 is a special case; reset line
                line = strLeftSpacing + CHAR_LEFT_RIGHT_BORDER + strSpacingInCell;
                break;
            case 9:
                line += "For: " + transfer.getTransferMessage() + strSpacingAfterMessage;
                break;
            case 10:
                line += "Amount: ";
                // if transfer is FROM current user
                if (transfer.getUserFrom().getId() == id) {
                    // add red font and a leading - sign for amount
                    line += ColorCodes.RED + "-" + n.format(transfer.getAmount()) + ColorCodes.RESET;
                    }
                else {line += n.format(transfer.getAmount());}
                line += strSpacingAfterAmount;
                break;
            case 11:
                line += "Status: " + transfer.getTransferStatusDesc() + strSpacingAfterStatus;
                break;
            case 12:
                line += "Type: " + transfer.getTransferTypeDesc() + strSpacingAfterType;
                break;
            case 13:
                line += formattedDate + strSpacingAfterDate;
                break;
        }


        line += CHAR_LEFT_RIGHT_BORDER;
        return line;
    }

    private static void determineGridSize(Transfer transfer, int id) {
            String userFrom = transfer.getUserFrom().getUsername();
            String userTo = transfer.getUserTo().getUsername();
            int lengthFromTo = "From: To: ".length();
            int lengthFor = "For: ".length();
            int lengthAmount = "Amount: $".length();
            int lengthStatus = "Status: ".length();
            int lengthType = "Type: ".length();
            formattedDate = getFormattedDate(transfer);

            lengthUsernameFrom = userFrom.length();
            lengthUsernameTo = userTo.length();

            // determine spacing inside cell--max of either padding + max message length OR padding + the Strings "From: <usernameFrom>" and "To: <usernameTo>" + 5 spaces
            numSpacesInCell = Math.max(NUM_SPACES_PADDING_LEFT_RIGHT * 2 + MAX_LENGTH_MESSAGE, NUM_SPACES_PADDING_LEFT_RIGHT * 2 + lengthFromTo + lengthUsernameFrom + lengthUsernameTo + 5);

            numSpacesBetweenAvatars = numSpacesInCell - (NUM_SPACES_PADDING_LEFT_RIGHT * 2) - (AVATAR_WIDTH * 2);
            numSpacesBetweenUsernames = numSpacesInCell - (NUM_SPACES_PADDING_LEFT_RIGHT * 2) - lengthFromTo - lengthUsernameFrom - lengthUsernameTo;
            numSpacesAfterMessage = numSpacesInCell - NUM_SPACES_PADDING_LEFT_RIGHT - lengthFor - transfer.getTransferMessage().length();
            numSpacesAfterAmount = numSpacesInCell - NUM_SPACES_PADDING_LEFT_RIGHT - lengthAmount - transfer.getAmount().toString().length();
            numSpacesAfterStatus = numSpacesInCell - NUM_SPACES_PADDING_LEFT_RIGHT - lengthStatus - transfer.getTransferStatusDesc().length();
            numSpacesAfterType = numSpacesInCell - NUM_SPACES_PADDING_LEFT_RIGHT - lengthType - transfer.getTransferTypeDesc().length();
            numSpacesAfterDate = numSpacesInCell - NUM_SPACES_PADDING_LEFT_RIGHT - formattedDate.length();

        // if transfer is FROM current user, subtract 1 from numSpacesAfterAmount (for - sign)
        if (transfer.getUserFrom().getId() == id) {
            numSpacesAfterAmount--;
        }
    }


    private static void setSpacing() {
        String spacing = "";
        for (int i = 0; i < NUM_SPACES_LEFT_OF_CELL; i++) {
            spacing += " ";
        }
        strLeftSpacing = spacing;

        spacing = "";
        for (int i = 0; i < NUM_SPACES_PADDING_LEFT_RIGHT; i++) {
            spacing += " ";
        }
        strSpacingPaddingLeftRight = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesBetweenAvatars; i++) {
            spacing += " ";
        }
        strSpacingBetweenAvatars = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesBetweenUsernames; i++) {
            spacing += " ";
        }
        strSpacingBetweenUsernames = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesInCell; i++) {
            spacing += " ";
        }
        strSpacingInCell = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterMessage; i++) {
            spacing += " ";
        }
        strSpacingAfterMessage = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterAmount; i++) {
            spacing += " ";
        }
        strSpacingAfterAmount = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterStatus; i++) {
            spacing += " ";
        }
        strSpacingAfterStatus = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterType; i++) {
            spacing += " ";
        }
        strSpacingAfterType = spacing;

        spacing = "";
        for (int i = 0; i < numSpacesAfterDate; i++) {
            spacing += " ";
        }
        strSpacingAfterDate = spacing;
    }


    private static String getFormattedDate(Transfer transfer) {
        // Parse the string dateTime into a LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(transfer.getTransferDateTime());

        // get month and convert to lowercase (default is uppercase)
        String month = dateTime.getMonth().toString().toLowerCase();

        // format month to have proper case (e.g., December)
        String firstCharMo = String.valueOf(month.charAt(0)).toUpperCase();
        month = firstCharMo + month.substring(1);

        // get day
        int day = dateTime.getDayOfMonth();

        // get year
        int year = dateTime.getYear();

        // get time
        String time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        return month + " " + day + "," + year + " " + time;
    }
}
