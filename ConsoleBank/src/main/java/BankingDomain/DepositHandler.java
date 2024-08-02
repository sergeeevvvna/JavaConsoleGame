package BankingDomain;

import java.util.Scanner;

public class DepositHandler implements IConsoleHandler {
    private final IAccountService accountService;
    private IConsoleHandler nextHandler;

    public DepositHandler(IAccountService accountService) {
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
        System.out.println("Выбрана операция депозита.");
        Scanner scanner = new Scanner(System.in);

        if (tryGetDepositAmount(scanner)) {
            System.out.print("Введите номер счета: ");
            if (scanner.hasNextInt()) {
                int accountNumber = scanner.nextInt();
                System.out.print("Введите PIN: ");
                if (scanner.hasNextInt()) {
                    int pin = scanner.nextInt();
                    System.out.print("Введите сумму для депозита: ");
                    double depositAmount = scanner.nextDouble();

                    if (accountService.deposit(accountNumber, pin, depositAmount)) {
                        System.out.printf("Депозит в размере %.2f выполнен успешно.%n", depositAmount);
                    } else {
                        System.out.println("Ошибка при выполнении депозита.");
                    }
                } else {
                    System.out.println("Неверный PIN. Введите корректный целочисленный PIN.");
                }
            } else {
                System.out.println("Неверный номер счета. Введите корректный целочисленный номер счета.");
            }
        } else {
            System.out.println("Некорректная сумма депозита.");
        }

        if (nextHandler != null) {
            nextHandler.handleRequest();
        }
    }

    private boolean tryGetDepositAmount(Scanner scanner) {
        System.out.print("Введите сумму для депозита: ");
        if (scanner.hasNextDouble()) {
            double depositAmount = scanner.nextDouble();
            return depositAmount > 0;
        } else {
            System.out.println("Некорректная сумма депозита.");
            return false;
        }
    }
}
