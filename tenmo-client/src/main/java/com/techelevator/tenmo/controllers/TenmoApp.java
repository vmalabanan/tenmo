package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.models.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.models.exceptions.InvalidAmountException;
import com.techelevator.tenmo.models.exceptions.InvalidTransferIdException;
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
        viewTransfers(false);
    }

    private void viewPendingRequests()
    {
        viewTransfers(true);
    }

    private void viewTransfers(boolean showOnlyPending) {
        // clear screen
        ViewTransfersPage.clearScreen();

        int id;

        var transfers = showOnlyPending ? transferService.getPendingTransfers()
                                                    : transferService.getAllTransfers();

        // if there are no pending requests
        if (transfers.size() == 0) {
            ViewTransfersPage.printAlertStyle("No transfers to display.", true);
            return;
        }

        // otherwise, display the transfers
        else {
            id = currentUser.getUser().getId();
            String header;
            if (showOnlyPending) {
                header = "Pending Transfers";
            }
            else {
                header = "Transfers";
            }
            ViewTransfersPage.displayTransfers(transfers, id, header);
        }

        // CHECK TRANSFER ID
        Transfer transfer = null;
        int transferId = -1;

        while (transfer == null) {
            transferId = showOnlyPending ? ViewTransfersPage.getSelection("Please enter transfer ID to approve/reject (0 to cancel): ")
                                         : ViewTransfersPage.getSelection("Please enter transfer ID to view details (0 to cancel): ");

            // break if user selects 0
            if (transferId == 0) {
                break;
            }

            transfer = checkTransferId(transfers, transferId);
        }

        // return to previous menu if user selects 0
        if (transferId == 0) {
            return;
        }

        if (!showOnlyPending) {
            viewTransferDetails(transfers, transferId, id);
        }

        else {
            // CHECK IF APPROVE/REJECT OPTION IS VALID
            int option = ViewTransfersPage.getApproveOrRejectChoice();

            while (option < 0 || option > 2) {
                ViewTransfersPage.printAlertStyle("Invalid selection. Please try again.", false);
                option = ViewTransfersPage.getSelection();
            }

            // go back to previous screen if user wants to cancel
            if (option == 0) {
                return;
            }
            // if user wants to approve transfer
            else if (option == 1) {
                // check if transfer amount > balance
                BigDecimal amount = transfer.getAmount();
                BigDecimal balance = accountService.getCurrentBalance();
                try {
                    if (amount.compareTo(balance) >= 1) {
                        throw new InsufficientFundsException(amount, balance);
                    }
                    else {
                        transfer.setTransferStatusId(2);
                    }
                } catch (InsufficientFundsException e) {
                    handleException(e);
                    ViewTransfersPage.pause();
                    viewTransfers(true);
                    return;
                }

            }
            // if user wants to reject transfer
            else if (option == 2){
                transfer.setTransferStatusId(3);
            }

            // send transfer request to the server
            Transfer newTransfer = transferService.editTransfer(transfer);

            // let the user know whether the transfer was successful
            transferOutcomeAlert(transfer);
        }

    }

    // View the details of an individual transfer.
    // Method takes in a list of all transfers and the transferId passed in by the user
    private void viewTransferDetails(List<Transfer> transfers, int transferId, int id) {
        // clear screen
        ViewTransferDetailsPage.clearScreen();

        ViewTransferDetailsPage.displayTransferDetails(transfers, transferId, id);
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

        ChangeAvatarPage.displayCurrentAvatarAndOptions(currentUser.getUser().getAvatar());

        int option = ChangeAvatarPage.getSelection("Please choose an option: ");

        // verify option selected
        while (option < 0 || option > 3) {
            ChangeAvatarPage.printAlertStyle("Invalid selection. Please try again.", false);
            option = ChangeAvatarPage.getSelection("Please choose an option: ");
        }

        // return to previous menu if user wants to cancel
        if (option == 0) {
            return;
        }

        // change avatar
        if (option == 1 || option == 3) {
            List<Avatar> avatars = avatarService.getAllAvatars();
            ChangeAvatarPage.displayAvatars(avatars);
            int selection = ChangeAvatarPage.getSelection();

            // verify option selected
            while (selection < 0 || selection > 6) {
                ChangeAvatarPage.printAlertStyle("Invalid selection. Please try again.", false);
                selection = ChangeAvatarPage.getSelection();
            }

            // return to previous menu if user wants to cancel
            if (selection == 0) {
                return;
            }

            Avatar avatar = ChangeAvatarPage.makeAvatarSelection(avatars, selection);
            Avatar newAvatar = avatarService.changeAvatar(avatar);

            // let user know if action was a success
            if (newAvatar != null) {
                ChangeAvatarPage.printAlertStyle("Avatar successfully changed to " + newAvatar.getAvatarDesc().toLowerCase(), true);
                // Set the currentUser's avatar
                // (because the avatar is only pulled from the server once, on login.
                // So when any changes are made, they have to be set here)
                currentUser.getUser().setAvatar(newAvatar);
            }
            else {
                ChangeAvatarPage.printAlertStyle("Avatar not changed. Please try again", true);
            }

        }
        // change avatar color
        if (option == 2 || option == 3) {
            changeAvatarColor();
        }
    }

    private void changeAvatarColor() {
        // clear screen
        ChangeAvatarColorPage.clearScreen();

        List<Color> colors = colorService.getAllColors();
        ChangeAvatarColorPage.displayColors(colors);
        int selection = ChangeAvatarColorPage.getSelection();

        // verify option selected
        while (selection < 0 || selection > 6) {
            ChangeAvatarColorPage.printAlertStyle("Invalid selection. Please try again.", false);
            selection = ChangeAvatarColorPage.getSelection();
        }

        // return to previous menu if user wants to cancel
        if (selection == 0) {
            return;
        }

        Color color = ChangeAvatarColorPage.makeColorSelection(colors, selection);
        Color newColor = colorService.changeColor(color);

        // let user know if action was a success
        if (newColor != null) {
            ChangeAvatarColorPage.printAlertStyle("Avatar color successfully changed to " + newColor.getColorDesc().toLowerCase(), true);
            // Set the currentUser's avatar color
            // (because the avatar, including color, is only pulled from the server once, on login.
            // So when any changes are made, they have to be set here)
            currentUser.getUser().getAvatar().setColor(newColor);
        }
        else {
            ChangeAvatarColorPage.printAlertStyle("Avatar color not changed. Please try again", true);
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
        Transfer transfer = MakeTransferPage.createTransferObject(user, amount, message, transferType);
        Transfer newTransfer = transferService.createTransfer(transfer);

        // let the user know whether the transfer was successful
        transferOutcomeAlert(newTransfer);
    }

    private void transferOutcomeAlert(Transfer transfer) {
        if (transfer != null) {
            // to format amount as money
            NumberFormat n = NumberFormat.getCurrencyInstance();

            // if transfer is a Send
            if (transfer.getTransferTypeId() == 2) {
                MakeTransferPage.printAlertStyle("Transfer successful. " + n.format(transfer.getAmount()) + " sent to user " + transfer.getUserTo().getUsername() + ", ID: " + transfer.getUserTo().getId() + ".", true);
            }

            // if transfer is a Request
            else {
                // if transfer was initiated by the user
                if (transfer.getUserTo().getId() == currentUser.getUser().getId()){
                        MakeTransferPage.printAlertStyle("Request successful. " + n.format(transfer.getAmount()) + " requested from user " + transfer.getUserFrom().getUsername() + ", ID: " + transfer.getUserFrom().getId() + ".", true);
                }
                // if transfer is a Request initiated by another user
                else  {
                    // if current user approved the request
                    if (transfer.getTransferStatusId() == 2) {
                        MakeTransferPage.printAlertStyle("Request " + transfer.getTransferId() + " approved. " + n.format(transfer.getAmount()) + " sent to user " + transfer.getUserTo().getUsername() + ", ID: " + transfer.getUserTo().getId() + ".", true);
                    }
                    // if current user rejected the request
                    else {
                        MakeTransferPage.printAlertStyle("Request " + transfer.getTransferId() + " rejected.", true);
                    }
                }
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

    private Transfer checkTransferId(List<Transfer> transfers, int transferId) {
        Transfer transfer;

        // check if there is a user in users with userId = selection
        transfer = getTransfer(transfers, transferId);

        // if not (i.e., selection isn't valid),
        // throw an InvalidTransferIdException
        try {
            if (transfer == null) {
                throw new InvalidTransferIdException(transferId);
            }
        } catch (Exception e) {
            handleException(e);
        }
        return transfer;
    }

    private Transfer getTransfer(List<Transfer> transfers, int transferId) {
        Transfer transfer = null;

        for (Transfer t : transfers) {
            if (t.getTransferId() == transferId) {
                transfer = t;
            }
        }
        return transfer;
    }

}
