package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.views.constants.ColorCodes;
import com.techelevator.tenmo.views.constants.Console;

import java.util.Scanner;


public abstract class BasePage
{
    public static final Scanner console = new Scanner(System.in);

    public static String getValue(String message)
    {
        print(message);
        return console.nextLine();
    }

    public static int getIntValue(String message)
    {
        String input =  getValue(message);
        int selection;

        try {
            selection = Integer.parseInt(input);
        } catch (NumberFormatException e)
        {
            selection = -1;
        }
        return selection;
    }

    public static void printHeader(String pageName)
    {
        printLine();
        printLine("*****************");
        printLine(pageName);
        printLine("*****************");
        printLine();
    }

    public static int getSelection() {
        return getIntValue("Please make a selection. (0 to cancel): ");
    }

    public static int getSelection(String message) {
        return getIntValue(message);
    }

    public static void invalidSelection() {
        printAlertStyle("Invalid selection. Please try again.", true);
    }

    public static void pause() {
        print("Press Enter to continue... ");
        console.nextLine();
    }

    public static void goodbye() {
        printAlertStyle("Logging off. Please come again!", false);
    }

    public static void printErrorMessage()
    {
        printAlertStyle("An error occurred. Check the log for details.", true);
    }


    // clear screen
    public static void clearScreen() {
        System.out.println(Console.CLEAR_SCREEN);
    }

    // prints
    public static void print(String line)
    {
        System.out.print(line);
    }

    public static void printRed(String line)
    {
        System.out.print(ColorCodes.RED + line + ColorCodes.RESET);
    }

    public static void printBlack(String line)
    {
        System.out.print(ColorCodes.BLACK + line + ColorCodes.RESET);
    }

    public static void printGreen(String line)
    {
        System.out.print(ColorCodes.GREEN + line + ColorCodes.RESET);
    }

    public static void printYellow(String line)
    {
        System.out.print(ColorCodes.YELLOW + line + ColorCodes.RESET);
    }

    public static void printBlue(String line)
    {
        System.out.print(ColorCodes.BLUE + line + ColorCodes.RESET);
    }

    public static void printPurple(String line)
    {
        System.out.print(ColorCodes.PURPLE + line + ColorCodes.RESET);
    }

    public static void printCyan(String line)
    {
        System.out.print(ColorCodes.CYAN + line + ColorCodes.RESET);
    }

    public static void printWhite(String line)
    {
        System.out.print(ColorCodes.WHITE + line + ColorCodes.RESET);
    }



    // print lines
    public static void printLine()
    {
        System.out.println();
    }

    public static void printLine(String line)
    {
        System.out.println(line);
    }

    public static void printRedLine(String line)
    {
        System.out.println(ColorCodes.RED + line + ColorCodes.RESET);
    }

    public static void printBlackLine(String line)
    {
        System.out.println(ColorCodes.BLACK + line + ColorCodes.RESET);
    }

    public static void printGreenLine(String line)
    {
        System.out.println(ColorCodes.GREEN + line + ColorCodes.RESET);
    }

    public static void printYellowLine(String line)
    {
        System.out.println(ColorCodes.YELLOW + line + ColorCodes.RESET);
    }

    public static void printBlueLine(String line)
    {
        System.out.println(ColorCodes.BLUE + line + ColorCodes.RESET);
    }

    public static void printPurpleLine(String line)
    {
        System.out.println(ColorCodes.PURPLE + line + ColorCodes.RESET);
    }

    public static void printCyanLine(String line)
    {
        System.out.println(ColorCodes.CYAN + line + ColorCodes.RESET);
    }

    public static void printWhiteLine(String line)
    {
        System.out.println(ColorCodes.WHITE + line + ColorCodes.RESET);
    }

    public static void printAlertStyle(String line, Boolean doPause)
    {
        System.out.println();
        System.out.println(ColorCodes.BLUE_BACKGROUND + ColorCodes.BLACK + line + ColorCodes.RESET);
        System.out.println();

        if (doPause) {
            pause();
        }
    }
}
