package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.views.*;

import java.util.List;

public class TenmoApp
{
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final UserOutput userOutput = new UserOutput();
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
        userOutput.printGreeting();
        loginMenu();
        if (currentUser != null)
        {
            mainMenu();
        }
    }

    private void loginMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null)
        {
            userOutput.printLoginMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                handleRegister();
            }
            else if (menuSelection == 2)
            {
                handleLogin();
            }
            else if (menuSelection != 0)
            {
                System.out.println("Invalid Selection");
                userOutput.pause();
            }
        }
    }

    private void handleRegister()
    {
        System.out.println("Please register a new user account");
        UserCredentials credentials = userOutput.promptForCredentials();
        if (authenticationService.register(credentials))
        {
            System.out.println("Registration successful. You can now login.");
        }
        else
        {
            userOutput.printErrorMessage();
        }
    }

    private void handleLogin()
    {
        var page = new UserCredentialsPage();
        UserCredentials credentials = page.getUserCredential("login");
        var authenticatedUser = authenticationService.login(credentials);

        if (authenticatedUser != null)
        {
            currentUser = authenticatedUser;
            AuthenticatedApiService.setAuthToken(currentUser.getToken());
        }
        else
        {
            page.printHeader("Error");
            page.printRedLine("The login credentials were incorrect");
        }
    }

    private void mainMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0)
        {
            userOutput.printMainMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                viewCurrentBalance();
            }
            else if (menuSelection == 2)
            {
                viewTransferHistory();
            }
            else if (menuSelection == 3)
            {
                viewPendingRequests();
            }
            else if (menuSelection == 4)
            {
                sendBucks();
            }
            else if (menuSelection == 5)
            {
                requestBucks();
            }
            else if (menuSelection == 6)
            {
                changeAvatar();
            }
            else if (menuSelection == 0)
            {
                continue;
            }
            else
            {
                System.out.println("Invalid Selection");
            }
            userOutput.pause();
        }
    }


    private void viewCurrentBalance()
    {
        var page = new CurrentBalancePage();
        var balance = accountService.getCurrentBalance();
        page.displayCurrentBalance(balance);

    }

    private void viewTransferHistory()
    {
        var page = new ViewTransfersPage();
        var transfers = transferService.getAllTransfers();
        int id = currentUser.getUser().getId();
        int transferId = page.displayTransfers(transfers, id, "Please enter transfer ID to view details (0 to cancel): ");

        if (transferId != 0) {
            viewTransferDetails(transfers, transferId, id);
        }

        mainMenu();
    }

    // View the details of an individual transfer.
    // Method takes in a list of all transfers and the transferId passed in by the user
    private void viewTransferDetails(List<Transfer> transfers, int transferId, int id) {
        var page = new ViewTransferDetailsPage();
        page.displayTransferDetails(transfers, transferId, id);
    }

    // TODO: combine with viewTransferHistory ?
    private void viewPendingRequests()
    {
        var page = new ViewTransfersPage();
        var transfers = transferService.getPendingTransfers();
        int id = currentUser.getUser().getId();
        int transferId = page.displayTransfers(transfers, id, "Please enter transfer ID to approve/reject (0 to cancel): ");

        if (transferId != 0) {
            int option = page.getPendingTransferOption();

            if (option != 0) {
                Transfer transfer = page.approveOrRejectTransfer(transfers, transferId, option); // should this be in MakeTransferPage instead?
                transferService.handleTransfer(transfer);
            }
        }

        mainMenu();

    }

    // TODO: Combine sendBucks and requestBucks
    private void sendBucks()
    {

        var page = new MakeTransferPage();
        var users = userService.getAllUsers();
        Transfer transfer = page.getTransferDetails(users, 2); // 2 is Send

        transferService.handleTransfer(transfer); // this returns a Transfer object but we're not doing anything with it right now
    }

    private void requestBucks()
    {
        var page = new MakeTransferPage();
        var users = userService.getAllUsers();
        Transfer transfer = page.getTransferDetails(users, 1); // 1 is Request

        transferService.handleTransfer(transfer);
    }

    private void changeAvatar() {
        var page = new ChangeAvatarPage(currentUser.getUser().getAvatar());
        page.displayCurrentAvatar();
        int choice = page.getChangeAvatarSelection();

        // change avatar
        if (choice == 1 || choice == 3) {
            List<Avatar> avatars = avatarService.getAllAvatars();
            page.displayAvatars(avatars);
            int selection = page.getSelection(avatars.size());

            if (selection != 0) {
                Avatar avatar = page.makeAvatarSelection(avatars, selection);
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
        var page = new ChangeAvatarColorPage(currentUser.getUser().getAvatar());

        List<Color> colors = colorService.getAllColors();
        page.displayColors(colors);
        int selection = page.getSelection(colors.size());

        if (selection != 0) {
            Color color = page.makeColorSelection(colors, selection);
            Color newColor = colorService.changeColor(color);
            // Set the currentUser's avatar color
            // (because the avatar, including color, is only pulled from the server once, on login.
            // So when any changes are made, they have to be set here)
            currentUser.getUser().getAvatar().setColor(newColor);

        }
    }

}
