package model;

import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String iban;
    private String currency;
    private String accountName;
    private long amount;
    private long creditLimitAmount;
    private boolean isCredit;




    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return  user;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }

    public long getCreditLimitAmount() {
        return creditLimitAmount;
    }

    public void setCreditLimitAmount(long creditLimitAmount) {
        this.creditLimitAmount = creditLimitAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", currency='" + currency + '\'' +
                ", accountName='" + accountName + '\'' +
                ", amount=" + amount +
                ", user=" + user +
                '}';
    }

    public void increaseBy(int amount) {
        this.amount+=amount;
    }

    public void decreaseBy(int amount) {
        this.amount-=amount;
    }
}
