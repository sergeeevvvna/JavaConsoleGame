package BankingInfrastructure;

import BankingDomain.Account;
import BankingDomain.IAccountRepository;
import BankingDomain.TransactionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PostgresAccountRepository implements IAccountRepository {
    private final String connectionString;

    public PostgresAccountRepository(String connectionString) {
        if (connectionString == null || connectionString.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid connection string.");
        }
        this.connectionString = connectionString;
    }

    @Override
    public void createAccount(int accountNumber, int pin, double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO accounts (account_number, pin, balance) VALUES (?, ?, ?)")) {

            statement.setInt(1, accountNumber);
            statement.setInt(2, pin);
            statement.setDouble(3, balance);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deposit(TransactionService transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement checkAccountExistsStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM accounts WHERE account_number = ?");
             PreparedStatement updateStatement = connection.prepareStatement(
                     "UPDATE accounts SET balance = balance + ? WHERE account_number = ?");
             PreparedStatement insertTransactionStatement = connection.prepareStatement(
                     "INSERT INTO transactions (account_number, transaction_type, amount, balance_after) VALUES (?, ?, ?, ?)")) {

            connection.setAutoCommit(false);

            // Check if account exists
            checkAccountExistsStatement.setInt(1, transaction.getAccountNumber());
            ResultSet accountExistsResult = checkAccountExistsStatement.executeQuery();
            if (!accountExistsResult.next() || accountExistsResult.getInt(1) == 0) {
                throw new IllegalStateException("Deposit failed. Account not found.");
            }

            // Perform deposit
            updateStatement.setDouble(1, transaction.getAmount());
            updateStatement.setInt(2, transaction.getAccountNumber());
            int updatedRows = updateStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalStateException("Deposit failed. Account not found.");
            }

            // Record transaction
            insertTransactionStatement.setInt(1, transaction.getAccountNumber());
            insertTransactionStatement.setString(2, "Deposit");
            insertTransactionStatement.setDouble(3, transaction.getAmount());
            insertTransactionStatement.setDouble(4, transaction.getAmount());
            insertTransactionStatement.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void withdraw(TransactionService transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement checkAccountExistsStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM accounts WHERE account_number = ?");
             PreparedStatement updateStatement = connection.prepareStatement(
                     "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?");
             PreparedStatement insertTransactionStatement = connection.prepareStatement(
                     "INSERT INTO transactions (account_number, transaction_type, amount, balance_after) VALUES (?, ?, ?, ?)")) {

            connection.setAutoCommit(false);

            // Check if account exists
            checkAccountExistsStatement.setInt(1, transaction.getAccountNumber());
            ResultSet accountExistsResult = checkAccountExistsStatement.executeQuery();
            if (!accountExistsResult.next() || accountExistsResult.getInt(1) == 0) {
                throw new IllegalStateException("Withdrawal failed. Account not found.");
            }

            // Perform withdrawal
            updateStatement.setDouble(1, transaction.getAmount());
            updateStatement.setInt(2, transaction.getAccountNumber());
            updateStatement.setDouble(3, transaction.getAmount());
            int updatedRows = updateStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalStateException("Withdrawal failed. Insufficient funds.");
            }

            // Record transaction
            insertTransactionStatement.setInt(1, transaction.getAccountNumber());
            insertTransactionStatement.setString(2, "Withdrawal");
            insertTransactionStatement.setDouble(3, transaction.getAmount());
            insertTransactionStatement.setDouble(4, transaction.getAmount());
            insertTransactionStatement.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO accounts (account_number, pin, balance) VALUES (?, ?, ?) "
                             + "ON CONFLICT (account_number) DO UPDATE SET pin = EXCLUDED.pin, balance = EXCLUDED.balance")) {

            statement.setInt(1, account.getAccountNumber());
            statement.setInt(2, account.getPin());
            statement.setDouble(3, account.getBalance());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Account> getAccount(int accountNumber, int pin) {
        Account account = null;

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM accounts WHERE account_number = ? AND pin = ?")) {

            statement.setInt(1, accountNumber);
            statement.setInt(2, pin);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int accountNum = resultSet.getInt("account_number");
                int accountPin = resultSet.getInt("pin");
                double balance = resultSet.getDouble("balance");

                account = new Account(accountNum, accountPin, balance);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.ofNullable(account);
    }
}
