package com.techelevator.tenmo.views.pages;

import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.views.grids.AvatarGrid;
import com.techelevator.tenmo.views.grids.ChangeAvatarGrid;

import java.util.List;

public class ChangeAvatarPage extends BasePage {

    public static void displayCurrentAvatarAndOptions(Avatar avatar) {
        ChangeAvatarGrid.printChangeAvatarGrid(avatar);
    }

    public static Avatar makeAvatarSelection(List<Avatar> avatars, int choice) {
        return avatars.get(choice - 1);
    }

    public static void displayAvatars(List<Avatar> avatars) {
        printHeader("Avatars");
        AvatarGrid.printAvatarGrid(avatars);
    }

}
