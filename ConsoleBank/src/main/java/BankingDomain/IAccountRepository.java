package BankingDomain;

import java.util.Optional;

public interface IAccountRepository {
    void createAccount(int accountNumber, int pin, double balance);

    void deposit(TransactionService transaction);

    void withdraw(TransactionService transaction);

    void saveAccount(Account account);
    Optional<Account> getAccount(int accountNumber, int pin);
}
