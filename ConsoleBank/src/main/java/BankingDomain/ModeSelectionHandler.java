package BankingDomain;

import java.util.Scanner;

public class ModeSelectionHandler implements IConsoleHandler {
    private IConsoleHandler nextHandler;

    public IConsoleHandler setNext(IConsoleHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public void handleRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите '1' для режима пользователя или '2' для режима администратора: ");

        if (scanner.hasNextInt()) {
            int modeSelection = scanner.nextInt();
            if (modeSelection == 1) {
                System.out.println("Выбран режим пользователя.");
            } else if (modeSelection == 2) {
                System.out.println("Выбран режим администратора.");
            } else {
                System.out.println("Неверный выбор режима. Завершение работы.");
            }
        } else {
            System.out.println("Неверный ввод. Завершение работы.");
        }

        if (nextHandler != null) {
            nextHandler.handleRequest();
        }
    }
}
