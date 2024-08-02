package BankingDomain;

public class TransactionService {
    private final int accountNumber;
    private final char type;
    private final double amount;

    public TransactionService(int accountNumber, char type, double amount) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public char getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
