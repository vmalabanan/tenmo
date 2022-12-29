package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.views.grids.AvatarGrid;

import java.util.List;

public class ChangeAvatarPage extends BasePage {

    public static void displayCurrentAvatar(Avatar avatar) {
        printLine("Current avatar:");
        DisplayAvatar displayAvatar = new DisplayAvatar(avatar);
        displayAvatar.printAvatar();
        printLine();
    }

    public static void changeAvatarOptions() {
        printLine("What do you want to do?");
        printLine("1: Change avatar");
        printLine("2: Change avatar color");
        printLine("3: Change both");
        printLine("0: Cancel");
    }

    public static int getChangeAvatarSelection() {
        changeAvatarOptions();
        return getSelection();
    }

    public static Avatar makeAvatarSelection(List<Avatar> avatars, int choice) {
        return avatars.get(choice - 1);
    }

    public static void displayAvatars(List<Avatar> avatars) {
        printHeader("Avatars");
        AvatarGrid.printAvatarGrid(avatars);
    }

}
