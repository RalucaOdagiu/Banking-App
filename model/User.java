package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User  {


    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;

    private String firstName;
    private String lastName;
    private String CNP;
    private String email;
    private String userName;
    private String password;
    private boolean isLoggedIn;

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Account> accountList = new ArrayList<>();

    public void addAccount (Account account){
        accountList.add(account);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", CNP='" + CNP + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Account getAccountByUserIndex(String bankAccountIndex) {
        int bankAccountIndexAsInt = Integer.parseInt(bankAccountIndex)-1;

        return accountList.get(bankAccountIndexAsInt);

    }

    public String getFullName() {
        return firstName+" "+lastName;
    }
}
