package BankingDomain;

import java.util.Scanner;

public class UserAuthenticationHandler implements IConsoleHandler {
    private final IAccountService accountService;
    private IConsoleHandler nextHandler;

    public UserAuthenticationHandler(IAccountService accountService) {
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
        System.out.println("Выбран режим пользователя.");
        Scanner scanner = new Scanner(System.in);

        int accountNumber, pin;
        if (tryGetAccountCredentials(scanner, accountNumber = 0, pin = 0)) {
            if (accountService.authenticateUser(accountNumber, pin)) {
                System.out.println("Аутентификация прошла успешно.");
            } else {
                System.out.println("Ошибка аутентификации. Неверный номер счета или PIN.");
            }
        }

        if (nextHandler != null) {
            nextHandler.handleRequest();
        }
    }

    private boolean tryGetAccountCredentials(Scanner scanner, int accountNumber, int pin) {
        System.out.print("Введите номер счета: ");
        if (scanner.hasNextInt()) {
            accountNumber = scanner.nextInt();
            System.out.print("Введите PIN: ");
            if (scanner.hasNextInt()) {
                pin = scanner.nextInt();
                return true;
            } else {
                System.out.println("Неверный PIN. Введите корректный целочисленный PIN.");
            }
        } else {
            System.out.println("Неверный номер счета. Введите корректный целочисленный номер счета.");
        }

        return false;
    }
}
