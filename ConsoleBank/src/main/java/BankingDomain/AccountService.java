package BankingDomain;

import java.util.List;
import java.util.Optional;

public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository) {
        if (accountRepository == null) {
            throw new IllegalArgumentException("AccountRepository cannot be null");
        }
        this.accountRepository = accountRepository;
    }

    public Account createAccount(int accountNumber, int pin, double initialBalance) {
       Account account = new Account(accountNumber, pin, initialBalance);
        accountRepository.saveAccount(account);
        return account;
    }

    public double getBalance(int accountNumber, int pin) {
        Optional<Account> account = accountRepository.getAccount(accountNumber, pin);
        return account.map(Account::getBalance).orElse(0.0);
    }

    public boolean deposit(int accountNumber, int pin, double amount) {
        Optional<Account> accountOpt = accountRepository.getAccount(accountNumber, pin);
        if (accountOpt.isEmpty()) {
            throw new IllegalStateException("Account not found");
        }

        Account account = accountOpt.get();
        account.updateBalance(account.getBalance() + amount);
        account.addTransaction(String.format("Deposited %.2f. New balance: %.2f", amount, account.getBalance()));

        accountRepository.saveAccount(account);
        return true;
    }

    public boolean withdraw(int accountNumber, int pin, double amount) {
        Optional<Account> accountOpt = accountRepository.getAccount(accountNumber, pin);

        if (accountOpt.isEmpty()) {
            throw new IllegalStateException("Account not found");
        }

        Account account = accountOpt.get();
        if (amount > account.getBalance()) {
            throw new IllegalStateException("Insufficient funds");
        }

        account.updateBalance(account.getBalance() - amount);
        account.addTransaction(String.format("Withdrawn %.2f. New balance: %.2f", amount, account.getBalance()));
        accountRepository.saveAccount(account);
        return true;
    }

    public List<String> getTransactionHistory(int accountNumber, int pin) {
        Optional<Account> account = accountRepository.getAccount(accountNumber, pin);
        return account.map(Account::getTransactionHistory).orElse(List.of());
    }

    public boolean authenticateUser(int accountNumber, int pin) {
        return accountRepository.getAccount(accountNumber, pin).isPresent();
    }
}
