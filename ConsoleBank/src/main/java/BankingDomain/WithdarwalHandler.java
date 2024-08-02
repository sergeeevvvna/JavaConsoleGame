package BankingDomain;

import java.util.Scanner;

public class WithdarwalHandler implements IConsoleHandler {
    private final IAccountService accountService;
    private IConsoleHandler nextHandler;

    public WithdarwalHandler(IAccountService accountService) {
        if (accountService == null) {
            throw new IllegalArgumentException("AccountService cannot be null");
        }
        this.accountService = accountService;
    }

    public IConsoleHandler setNext(IConsoleHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public void handleRequest() {
        System.out.println("Выбрана операция снятия средств.");
        Scanner scanner = new Scanner(System.in);

        double withdrawalAmount;
        if (tryGetWithdrawalAmount(scanner, withdrawalAmount = 0)) {
            System.out.print("Введите номер счета: ");
            if (scanner.hasNextInt()) {
                int accountNumber = scanner.nextInt();
                System.out.print("Введите PIN: ");
                if (scanner.hasNextInt()) {
                    int pin = scanner.nextInt();

                    if (accountService.withdraw(accountNumber, pin, withdrawalAmount)) {
                        System.out.printf("Снятие средств в размере %.2f выполнено успешно.%n", withdrawalAmount);
                    } else {
                        System.out.println("Ошибка при выполнении снятия средств.");
                    }
                } else {
                    System.out.println("Неверный PIN. Введите корректный целочисленный PIN.");
                }
            } else {
                System.out.println("Неверный номер счета. Введите корректный целочисленный номер счета.");
            }
        } else {
            System.out.println("Некорректная сумма для снятия средств.");
        }

        if (nextHandler != null) {
            nextHandler.handleRequest();
        }
    }

    private boolean tryGetWithdrawalAmount(Scanner scanner, double withdrawalAmount) {
        System.out.print("Введите сумму для снятия средств: ");
        if (scanner.hasNextDouble()) {
            withdrawalAmount = scanner.nextDouble();
            return withdrawalAmount > 0;
        } else {
            System.out.println("Некорректная сумма для снятия средств.");
            return false;
        }
    }
}
