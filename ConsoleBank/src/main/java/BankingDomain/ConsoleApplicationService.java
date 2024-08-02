package BankingDomain;

public class ConsoleApplicationService implements IApplicationService {
    private final IConsoleHandler rootHandler;
    private final IAccountService accountService;

    public ConsoleApplicationService(IConsoleHandler rootHandler, IAccountService accountService) {
        if (rootHandler == null) {
            throw new IllegalArgumentException("RootHandler cannot be null");
        }
        if (accountService == null) {
            throw new IllegalArgumentException("AccountService cannot be null");
        }
        this.rootHandler = rootHandler;
        this.accountService = accountService;
        configureHandlers();
    }

    public void run() {
        System.out.println("Добро пожаловать в банковское приложение!");
        rootHandler.handleRequest();
    }

    private void configureHandlers() {
       UserAuthenticationHandler userAuthenticationHandler = new UserAuthenticationHandler(accountService);
       DepositHandler depositHandler = new DepositHandler(accountService);
       WithdarwalHandler withdrawalHandler = new WithdarwalHandler(accountService);

        userAuthenticationHandler.setNext(depositHandler)
                .setNext(withdrawalHandler);

        rootHandler.setNext((IConsoleHandler) userAuthenticationHandler);
    }
}
