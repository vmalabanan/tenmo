package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.models.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.models.exceptions.InvalidAmountException;
import com.techelevator.tenmo.models.exceptions.InvalidUserIdException;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.views.pages.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
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

        // log-in menu
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
            // get current user's balance to display on main menu page
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

    private void viewTransferHistory()
    {
        // clear screen
        ViewTransfersPage.clearScreen();

        var transfers = transferService.getAllTransfers();

        // if there are no transfers
        if (transfers.size() == 0) {
            ViewTransfersPage.printAlertStyle("No transfers to display.", true);
            return;
        }

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

        // if there are no pending requests
        if (transfers.size() == 0) {
            ViewTransfersPage.printAlertStyle("No transfers to display.", true);
            return;
        }

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

    private void sendBucks()
    {
        makeTransfer(2);
    }


    private void requestBucks()
    {
        makeTransfer(1);
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

    // checks to see whether the selection matches one of the userIds in users
    private User getUser(List<User> users, int selection) {
        User user = null;

        for (User u : users) {
            if (u.getId() == selection) {
                user = u;
            }
        }
        return user;
    }

    private void makeTransfer(int transferType) {
        // clear screen
        MakeTransferPage.clearScreen();

        // get all users and display them
        var users = userService.getAllUsers();

        // if there are no users
        if (users.size() == 0) {
            MakeTransferPage.printAlertStyle("No users to display.", true);
            return;
        }

        MakeTransferPage.printUserList(users);

        // CHECK USERID INPUT
        User user = null;
        int selection = -1;

        while (user == null) {
            String message = transferType == 2 ? "Enter ID of user you are sending to (0 to cancel): " // if Send
                    : "Enter ID of user you are requesting from (0 to cancel): "; // if Request
            selection = MakeTransferPage.getSelection(message);

            // break if user selects 0
            if (selection == 0) {
                break;
            }
            user = checkUserId(users, selection);
        }

        // go back to previous menu if user selects 0
        if (selection == 0) {
            return;
        }

        // CHECK AMOUNT INPUT
        BigDecimal amount;
        // if Send
        if (transferType == 2) {
            // get current user's balance to compare to amount entered (should this be done on server side?)
            BigDecimal balance = accountService.getCurrentBalance();
            amount = checkAmount(balance);
        }
        else {
            amount = checkAmount();
        }

        // go back to previous menu if user selects 0
        if (amount.equals(BigDecimal.ZERO)) {
            return;
        }

        // GET MESSAGE
        String message = MakeTransferPage.getValue("What's it for? (0 to cancel): ");

        // go back to previous menu if user selects 0
        if (message.equals("0")) {
            return;
        }

        // create the transfer object and send to the server side
        Transfer transfer = MakeTransferPage.createTransferObject(user, amount, message,transferType);
        Transfer newTransfer = transferService.handleTransfer(transfer);

        // let the user know whether the transfer was successful
        transferOutcomeAlert(newTransfer, transferType);
    }

    private void transferOutcomeAlert(Transfer transfer, int transferType) {
        if (transfer != null) {
            // to format amount as money
            NumberFormat n = NumberFormat.getCurrencyInstance();
            if (transferType == 2) {
                MakeTransferPage.printAlertStyle("Transfer successful. " + n.format(transfer.getAmount()) + " sent to user " + transfer.getUserTo().getUsername() + ", ID: " + transfer.getUserTo().getId() + ".", true);
            }
            else {
                MakeTransferPage.printAlertStyle("Request successful. " + n.format(transfer.getAmount()) + " requested from user " + transfer.getUserFrom().getUsername() + ", ID: " + transfer.getUserFrom().getId() + ".", true);
            }
        } else {
            MakeTransferPage.printAlertStyle("Request not successful. Please try again.", true);
        }

    }

    private User checkUserId(List<User> users, int selection) {
        User user;

        // check if there is a user in users with userId = selection
        user = getUser(users, selection);

        // if not (i.e., selection isn't valid),
        // throw an InvalidUserIdException
        try {
            if (user == null) {
                throw new InvalidUserIdException(selection);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return user;
    }

    // amount validation for Sends
    private BigDecimal checkAmount(BigDecimal balance) {
        boolean isAmountValid = false;
        BigDecimal amount = null;

        while (!isAmountValid) {
            try {
                amount = new BigDecimal(MakeTransferPage.getValue("Enter amount (0 to cancel): $"));
                // if amount is greater than available balance, throw an InsufficientFundsException
                if (amount.compareTo(balance) >= 1) {
                    throw new InsufficientFundsException(amount, balance);
                }
                // if amount is less than or equal to 0, throw an InvalidAmountException
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InvalidAmountException(amount);
                }
                isAmountValid = true;

            } catch (Exception e) {
                handleException(e);
            }
        }

        return amount;
    }

    // amount validation for requests
    private BigDecimal checkAmount() {
        boolean isAmountValid = false;
        BigDecimal amount = null;

        while (!isAmountValid) {
            try {
                amount = new BigDecimal(MakeTransferPage.getValue("Enter amount (0 to cancel): $"));

                // if amount is less than or equal to 0, throw an InvalidAmountException
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InvalidAmountException(amount);
                }
                isAmountValid = true;

            } catch (Exception e) {
                handleException(e);
            }
        }

        return amount;
    }


    private void handleException(Exception e) {
        BasePage.printAlertStyle(e.getMessage() + " Please try again.", false);
    }

}
