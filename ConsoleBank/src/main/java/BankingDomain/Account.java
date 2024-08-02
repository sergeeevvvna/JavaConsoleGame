package BankingDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private final List<String> transactionHistory = new ArrayList<>();
    private final int accountNumber;
    private final int pin;
    private double balance;

    public Account(int accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    public void updateBalance(double newBalance) {
        this.balance = newBalance;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}
