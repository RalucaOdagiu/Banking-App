import dao.AccountDao;
import dao.UserDao;
import model.Account;
import model.User;
import service.IOService;

import java.util.List;
import java.util.Random;

public class BankingApp {

    private IOService ioService;
    private UserDao userDao;
    private AccountDao accountDao;

    public BankingApp() {

        ioService = new IOService();
        userDao = new UserDao();
        accountDao = new AccountDao();

    }

    public void start() {
        while (true) {
            ioService.displayUnauthenticatedUserMenu();
            String userInput = ioService.getUserInput();

            process(userInput);
        }
    }

    private void process(String userInput) {
        System.out.println(userInput);

        if (userInput.equals("1")) {
            register();
        } else if (userInput.equals("2")) {
            User loggedInUser = login();
            if (loggedInUser != null) {
                loggedInUser.setLoggedIn(true);
                while (loggedInUser.getIsLoggedIn()) {
                    ioService.displayAuthenticatedUserMenu();
                    userInput = ioService.getUserInput();

                    processLoggedIn(userInput, loggedInUser);
                }
            }
        }
    }

    private void processLoggedIn(String userInput, User user) {
        switch (userInput) {
            case "1": {
                viewPortfolio(user);
                break;
            }
            case "2": {
                transferMoneyFrom(user);
                break;
            }
            case "3": {
                depositCash(user);
                break;
            }
            case "4": {
                createDebitAccountFlow(user);
                break;
            }
            case "5": {
                createCreditAccountFlow(user);
                break;
            }
            case "6": {
                transferMoneyWithinAccountsOf(user);
                break;
            }
            case "0": {
                logOut(user);
                break;
            }
        }
    }

    private void transferMoneyWithinAccountsOf(User user) {
        //1- identificam contul sursa de unde vrem sa transferam
        ioService.displayAccounts(user.getAccountList());
        String sourceAccountIndex = ioService.getUserInput();

        //2- introducem suma transferului
        String amountToTransfer = ioService.getField("amount to transfer");

        //3- afisam conturile existente
        ioService.displayAccounts(user.getAccountList());
        String destinationAccountIndex = ioService.getUserInput();

        //4- validam
        if (!validateSameUserTransfer(sourceAccountIndex, amountToTransfer, destinationAccountIndex, user.getAccountList())) {
            ioService.displayValidationFieldError();
            return;
        }

        //5- performTransfer();
        performTransfer(user, amountToTransfer,sourceAccountIndex,destinationAccountIndex);
    }

    private void performTransfer(User user, String amountToTransfer, String sourceAccountIndex, String destinationAccountIndex) {
        int amount =Integer.parseInt(amountToTransfer);
        Account source = user.getAccountByUserIndex(sourceAccountIndex);
        Account destination = user.getAccountByUserIndex(destinationAccountIndex);
        source.decreaseBy(amount);
        destination.increaseBy(amount);
        userDao.updateEntity(user);
    }

    private boolean validateSameUserTransfer(String sourceAccountIndex, String amountToTransfer, String destinationAccountIndex, List<Account> accountList) {
        try {
            int sourceIndex = Integer.parseInt(sourceAccountIndex);
            int destinationIndex = Integer.parseInt(destinationAccountIndex);

            int amount = Integer.parseInt(amountToTransfer);
            if (sourceIndex <= 0 || sourceIndex > accountList.size()||
                    destinationIndex <= 0 || destinationIndex > accountList.size()||
            sourceIndex==destinationIndex) {
                ioService.errorMessageField("account index");
                return false;
            }
            Account sourceAccount = accountList.get(sourceIndex - 1);

            if (sourceAccount.getAmount() < amount || amount <= 0) {

                ioService.errorMessageField("amount");
                return false;
            }

        } catch (Exception exception) {
            ioService.errorMessageField("numeric");

            return false;
        }
        return true;
    }

    private void createCreditAccountFlow(User user) {
        String accountName = ioService.getField("account name");
        String currency = ioService.getField("currency");
        String creditLimit = ioService.getField("credit limit");
        long creditLimitAsLong = Long.parseLong(creditLimit);


        Account account = new Account();
        account.setAccountName(accountName);
        account.setCurrency(currency);
        account.setAmount(creditLimitAsLong);
        account.setIban(generateIban());
        account.setUser(user);
        account.setCredit(true);
        account.setCreditLimitAmount(creditLimitAsLong);
        user.addAccount(account);
        accountDao.saveEntity(account);
        ioService.displayConfirmationMessage();

    }

    private void logOut(User user) {
        user.setLoggedIn(false);
        ioService.logOutMessage();
    }


    private void transferMoneyFrom(User user) {
        //1- identificam contul sursa de unde vrem sa transferam
        ioService.displayAccounts(user.getAccountList());
        String userAccountIndex = ioService.getUserInput();

        //2- introducem suma transferului
        String amountToTransfer = ioService.getField("amount to transfer");

        //3- introducem IBAN-ul destinatarului
        String destinataryIban = ioService.getField("destinatary IBAN");

        //4- validam ibanul, suma transferului (currency)
        if (!validate(userAccountIndex, amountToTransfer, destinataryIban, user.getAccountList())) {
            ioService.displayValidationFieldError();
            return;
        }

        //5- performTransfer();
        performTransfer(user.getAccountByUserIndex(userAccountIndex), amountToTransfer, destinataryIban);


    }

    private void performTransfer(Account sourceAccount, String amountToTransfer, String destinataryIban) {
        Account destinationAccount = accountDao.findByIban(destinataryIban);
        int amount = Integer.parseInt(amountToTransfer);
        destinationAccount.increaseBy(amount);
        sourceAccount.decreaseBy(amount);
        accountDao.updateEntity(sourceAccount, destinationAccount);
        ioService.displayConfirmationMessage();


    }


    private boolean validate(String userAccountIndex, String amountToTransfer, String destinataryIban, List<Account> accountList) {
        try {
            int accountIndex = Integer.parseInt(userAccountIndex);
            int amount = Integer.parseInt(amountToTransfer);
            if (accountIndex <= 0 || accountIndex > accountList.size()) {
                ioService.errorMessageField("account index");
                return false;
            }
            Account account = accountList.get(accountIndex - 1);

            System.out.println("sursa " + account);
            if (account.getAmount() < amount || amount <= 0) {

                ioService.errorMessageField("amount");
                return false;
            }
            Account accountByIban = accountDao.findByIban(destinataryIban);
            if (accountByIban == null) {
                ioService.errorMessageField("IBAN");
                return false;
            }

        } catch (Exception exception) {
            ioService.errorMessageField("numeric");

            return false;
        }
        return true;

    }

    private void viewPortfolio(User user) {
        ioService.displayAccountsInfromation(user);

    }

    private void depositCash(User user) {
        ioService.displayAccounts(user.getAccountList());
        String bankAccountIndex = ioService.getUserInput();
        String amount = ioService.getField("amount");
        System.out.println("User-ul vrea sa introduca " + amount + " in contul de pe indexul " + bankAccountIndex + "-1");
        addAmount(amount, user.getAccountByUserIndex(bankAccountIndex));
        ioService.displayConfirmationMessage();
    }

    private void addAmount(String amount, Account account) {
        long currentAmount = account.getAmount();
        long newAmount = currentAmount + Integer.parseInt(amount);
        account.setAmount(newAmount);
        accountDao.updateEntity(account);

    }

    private void createDebitAccountFlow(User user) {

        String accountName = ioService.getField("account name");
        String currency = ioService.getField("currency");

        Account account = new Account();
        account.setAccountName(accountName);
        account.setCurrency(currency);
        account.setIban(generateIban());
        account.setUser(user);
        user.addAccount(account);
        accountDao.saveEntity(account);
        ioService.displayConfirmationMessage();

    }

    private String generateIban() {

        String iban = "RO12INGB";
        Random random = new Random();
        for (int index = 0; index < 16; index++) {
            iban = iban + random.nextInt(10);
        }
        return iban;

    }

    private User login() {

        String userName = ioService.getField("username");
        String password = ioService.getField("password");
        System.out.println("Trying to login with " + userName + " " + password);
        User user = userDao.findByUsername(userName);
        if (user == null) {
            System.out.println("Utilizatorul nu exista!");
            return null;
        }

        System.out.println(user.getPassword() + " E parla user-ului");
        System.out.println("User-ul inceraca sa se logheze cu: " + password);

        if (user.getPassword().equals(password)) {
            System.out.println("Utilizatorul s-a logat cu succes!");
            return user;
        } else {
            System.out.println("Nu s-a putut loga \n");
            return null;
        }
    }

    private void register() {

        String firstName = ioService.getField("first name");
        String lastName = ioService.getField("last name");
        String cnp = ioService.getField("CNP");
        String email = ioService.getField("email");
        String userName = ioService.getField("username");
        String password = ioService.getField("password");

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCNP(cnp);
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(password);

        System.out.println(user + "\n");
        userDao.saveEntity(user);
    }
}
