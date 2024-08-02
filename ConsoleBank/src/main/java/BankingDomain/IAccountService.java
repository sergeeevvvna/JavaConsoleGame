package BankingDomain;

import java.util.List;

public interface IAccountService {
    Account createAccount(int accountNumber, int pin, double initialBalance);
    double getBalance(int accountNumber, int pin);
    boolean deposit(int accountNumber, int pin, double amount);
    boolean withdraw(int accountNumber, int pin, double amount);
    List<String> getTransactionHistory(int accountNumber, int pin);
    boolean authenticateUser(int accountNumber, int pin);
}
