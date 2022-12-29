package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.views.pages.*;

import java.math.BigDecimal;
import java.util.List;

public class TenmoApp
{
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();

    private AuthenticatedUser currentUser;
    private UserService userService = new UserService();
    private TransferService transferService = new TransferService();
    private AvatarService avatarService = new AvatarService();
    private ColorService colorService = new ColorService();

    public TenmoApp() {
        AuthenticatedApiService.setBaseUrl(API_BASE_URL);
    }

    public void run()
    {
        // clear screen
        WelcomePage.clearScreen();

        // Tenmo Logo
        WelcomePage.printLogo();

        // log in menu
        loginMenu();

        // main menu
        if (currentUser != null)
        {
            mainMenu();
        }
    }

    private void loginMenu()
    {
        int menuSelection = -1;

        // keep looping until user has logged in
        while (currentUser == null && menuSelection != 0) {
            // clear screen
            WelcomePage.clearScreen();

            WelcomePage.printLoginMenu();

            menuSelection = WelcomePage.getSelection("Please choose an option: ");

            switch (menuSelection) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 0:
                    WelcomePage.goodbye();
                    break;
                default:
                    WelcomePage.invalidSelection();

                    // show welcome screen again
                    run();
            }
        }

    }

    private void handleRegister()
    {
        // clear screen
        UserCredentialsPage.clearScreen();

        UserCredentials credentials = UserCredentialsPage.getUserCredential("Register");
        if (authenticationService.register(credentials))
        {
            UserCredentialsPage.printAlertStyle("Registration successful. You can now login.", true);
        }
        else
        {
            UserCredentialsPage.printErrorMessage();
        }
    }

    private void handleLogin()
    {
        // clear screen
        UserCredentialsPage.clearScreen();

        UserCredentials credentials = UserCredentialsPage.getUserCredential("Log in");
        var authenticatedUser = authenticationService.login(credentials);

        if (authenticatedUser != null)
        {
            currentUser = authenticatedUser;
            AuthenticatedApiService.setAuthToken(currentUser.getToken());
        }
        // if credentials aren't correct, show alert and go back to first menu page
        else
        {
            UserCredentialsPage.printAlertStyle("Incorrect username or password. Please try again.", true);
        }
    }

    private void mainMenu()
    {
        // clear screen
        MainMenuPage.clearScreen();

        int menuSelection = -1;
        while (menuSelection != 0)
        {
            // get current user's balance
            BigDecimal balance = accountService.getCurrentBalance();

            MainMenuPage.printMenu(currentUser.getUser(), balance);

            menuSelection = MainMenuPage.getSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                viewTransferHistory();
            }
            else if (menuSelection == 2)
            {
                viewPendingRequests();
            }
            else if (menuSelection == 3)
            {
                sendBucks();
            }
            else if (menuSelection == 4)
            {
                requestBucks();
            }
            else if (menuSelection == 5)
            {
                changeAvatar();
            }
            else if (menuSelection == 0)
            {
                MainMenuPage.goodbye();
            }
            else
            {
                MainMenuPage.invalidSelection();
            }
        }
    }


    private void viewCurrentBalance()
    {
        var balance = accountService.getCurrentBalance();
        CurrentBalancePage.displayCurrentBalance(balance);

    }

    private void viewTransferHistory()
    {
        // clear screen
        ViewTransfersPage.clearScreen();

        var transfers = transferService.getAllTransfers();
        int id = currentUser.getUser().getId();
        int transferId = ViewTransfersPage.displayTransfers(transfers, id, "Please enter transfer ID to view details (0 to cancel): ");

        if (transferId != 0) {
            viewTransferDetails(transfers, transferId, id);
        }

        mainMenu();
    }

    // View the details of an individual transfer.
    // Method takes in a list of all transfers and the transferId passed in by the user
    private void viewTransferDetails(List<Transfer> transfers, int transferId, int id) {
        // clear screen
        ViewTransferDetailsPage.clearScreen();

        ViewTransferDetailsPage.displayTransferDetails(transfers, transferId, id);
    }

    // TODO: combine with viewTransferHistory ?
    private void viewPendingRequests()
    {
        // clear screen
        ViewTransfersPage.clearScreen();

        var transfers = transferService.getPendingTransfers();
        int id = currentUser.getUser().getId();
        int transferId = ViewTransfersPage.displayTransfers(transfers, id, "Please enter transfer ID to approve/reject (0 to cancel): ");

        if (transferId != 0) {
            int option = ViewTransfersPage.getPendingTransferOption();

            if (option != 0) {
                Transfer transfer = ViewTransfersPage.approveOrRejectTransfer(transfers, transferId, option); // should this be in MakeTransferPage instead?
                transferService.handleTransfer(transfer);
            }
        }

        mainMenu();

    }

    // TODO: Combine sendBucks and requestBucks
    private void sendBucks()
    {
        // clear screen
        MakeTransferPage.clearScreen();

        var users = userService.getAllUsers();
        Transfer transfer = MakeTransferPage.getTransferDetails(users, 2); // 2 is Send

        transferService.handleTransfer(transfer); // this returns a Transfer object but we're not doing anything with it right now
    }

    private void requestBucks()
    {
        // clear screen
        MakeTransferPage.clearScreen();

        var users = userService.getAllUsers();
        Transfer transfer = MakeTransferPage.getTransferDetails(users, 1); // 1 is Request

        transferService.handleTransfer(transfer);
    }

    private void changeAvatar() {
        // clear screen
        ChangeAvatarPage.clearScreen();

        ChangeAvatarPage.displayCurrentAvatar(currentUser.getUser().getAvatar());
        int choice = ChangeAvatarPage.getChangeAvatarSelection();

        // change avatar
        if (choice == 1 || choice == 3) {
            List<Avatar> avatars = avatarService.getAllAvatars();
            ChangeAvatarPage.displayAvatars(avatars);
            int selection = ChangeAvatarPage.getSelection();

            if (selection != 0) {
                Avatar avatar = ChangeAvatarPage.makeAvatarSelection(avatars, selection);
                Avatar newAvatar = avatarService.changeAvatar(avatar);
                // Set the currentUser's avatar
                // (because the avatar is only pulled from the server once, on login.
                // So when any changes are made, they have to be set here)
                currentUser.getUser().setAvatar(newAvatar);

            }
        }
        // change avatar color
        if (choice == 2 || choice == 3) {
            changeAvatarColor();
        }
        // return to menu
        mainMenu();
    }

    private void changeAvatarColor() {
        // clear screen
        ChangeAvatarColorPage.clearScreen();

        List<Color> colors = colorService.getAllColors();
        ChangeAvatarColorPage.displayColors(colors);
        int selection = ChangeAvatarColorPage.getSelection();

        if (selection != 0) {
            Color color = ChangeAvatarColorPage.makeColorSelection(colors, selection);
            Color newColor = colorService.changeColor(color);
            // Set the currentUser's avatar color
            // (because the avatar, including color, is only pulled from the server once, on login.
            // So when any changes are made, they have to be set here)
            currentUser.getUser().getAvatar().setColor(newColor);

        }
    }

}
