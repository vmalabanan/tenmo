package com.techelevator.tenmo.views;

import com.techelevator.tenmo.views.constants.ColorCodes;

import java.util.Scanner;


public abstract class BasePage
{
    public static final Scanner console = new Scanner(System.in);

    public String getValue(String message)
    {
        printYellow(message);
        return console.nextLine();
    }
    public int getIntValue(String message)
    {
        var value =  getValue(message);
        return Integer.parseInt(value);
    }

    public void printHeader(String pageName)
    {
        printLine();
        printCyanLine("*****************");
        printRedLine(pageName);
        printCyanLine("*****************");
        printLine();
    }


    // prints
    public void print(String line)
    {
        System.out.print(line);
    }

    public void printRed(String line)
    {
        System.out.print(ColorCodes.RED + line + ColorCodes.RESET);
    }

    public void printBlack(String line)
    {
        System.out.print(ColorCodes.BLACK + line + ColorCodes.RESET);
    }

    public void printGreen(String line)
    {
        System.out.print(ColorCodes.GREEN + line + ColorCodes.RESET);
    }

    public void printYellow(String line)
    {
        System.out.print(ColorCodes.YELLOW + line + ColorCodes.RESET);
    }

    public void printBlue(String line)
    {
        System.out.print(ColorCodes.BLUE + line + ColorCodes.RESET);
    }

    public void printPurple(String line)
    {
        System.out.print(ColorCodes.PURPLE + line + ColorCodes.RESET);
    }

    public void printCyan(String line)
    {
        System.out.print(ColorCodes.CYAN + line + ColorCodes.RESET);
    }

    public void printWhite(String line)
    {
        System.out.print(ColorCodes.WHITE + line + ColorCodes.RESET);
    }


    // print lines

    public void printLine()
    {
        System.out.println();
    }

    public void printLine(String line)
    {
        System.out.println(line);
    }

    public void printRedLine(String line)
    {
        System.out.println(ColorCodes.RED + line + ColorCodes.RESET);
    }

    public void printBlackLine(String line)
    {
        System.out.println(ColorCodes.BLACK + line + ColorCodes.RESET);
    }

    public void printGreenLine(String line)
    {
        System.out.println(ColorCodes.GREEN + line + ColorCodes.RESET);
    }

    public void printYellowLine(String line)
    {
        System.out.println(ColorCodes.YELLOW + line + ColorCodes.RESET);
    }

    public void printBlueLine(String line)
    {
        System.out.println(ColorCodes.BLUE + line + ColorCodes.RESET);
    }

    public void printPurpleLine(String line)
    {
        System.out.println(ColorCodes.PURPLE + line + ColorCodes.RESET);
    }

    public void printCyanLine(String line)
    {
        System.out.println(ColorCodes.CYAN + line + ColorCodes.RESET);
    }

    public void printWhiteLine(String line)
    {
        System.out.println(ColorCodes.WHITE + line + ColorCodes.RESET);
    }



}
