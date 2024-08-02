package ConsoleApp;

import BankingDomain.*;
import BankingInfrastructure.PostgresAccountRepository;

public class Program {
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "qwerty";

        String connectionString = jdbcUrl + "?user=" + username + "&password=" + password;

        IAccountRepository accountRepository = new PostgresAccountRepository(connectionString);
        IAccountService accountService = new AccountService(accountRepository);

        IConsoleHandler modeSelectionHandler = new ModeSelectionHandler();
        IApplicationService applicationService = new ConsoleApplicationService(modeSelectionHandler, accountService);

        applicationService.run();
    }
}
