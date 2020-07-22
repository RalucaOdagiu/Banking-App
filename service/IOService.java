package service;

import model.Account;
import model.User;


import java.util.List;
import java.util.Scanner;

public class IOService {

    private Scanner scanner = new Scanner(System.in);

    public void displayUnauthenticatedUserMenu(){
        System.out.println("Hello dear user, please select one of the following options: ");
        System.out.println("1 - Register");
        System.out.println("2 - Login");
        System.out.print("Your answer: ");

    }


    public String getUserInput() {

        return scanner.nextLine();
    }
    public String getField(String field) {

        System.out.print("Please insert "+field+": ");
        return scanner.nextLine();
    }

    public void displayAuthenticatedUserMenu() {
        System.out.println("Please select one of the following options: \n");
        System.out.println("1 - View portfolio and balance");
        System.out.println("2 - Transfer money");
        System.out.println("3 - Deposit cash at ATM ");
        System.out.println("4 - Create debit account ");
        System.out.println("5 - Create credit account ");
        System.out.println("6 - Transfer money within your accounts ");
        System.out.println("0 - Log out ");
        System.out.print("Your answer: \n");

    }

    public void displayAccounts(List<Account> accountList) {
        System.out.println("Please select one of the following accounts: ");
        for(int index = 0; index<accountList.size();index++){
            System.out.println((index+1) +" - "+accountList.get(index).getAccountName());
        }
        System.out.println("Your answer is: ");
    }

    public void displayConfirmationMessage() {
        System.out.println("Operation was successfully executed!");
    }

    public void displayAccountsInfromation(User user) {
        System.out.println("Account information for user: "+ user.getFullName());
        System.out.println("Account name \t\t\t Amount \t\t\t IBAN \t\t\t Type");
        for(Account account : user.getAccountList()){
            String accountType = account.isCredit()?"credit":"debit";
            System.out.println(account.getAccountName()+ "\t \t \t" + account.getAmount()+" "+account.getCurrency()+"\t\t\t "+ account.getIban()+"\t\t\t"+accountType);
        }
    }

    public void displayValidationFieldError() {
        System.out.println("One of the fields has an invalid value, please try again.");
    }

    public void errorMessageField(String fieldName) {
        System.out.println("The "+fieldName+" field is not valid!");
    }

    public void logOutMessage() {
        System.out.println("You have been successfully logged out!");
    }
}
