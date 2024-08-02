package BankingDomain;

public interface IConsoleHandler {
    IConsoleHandler setNext(IConsoleHandler handler);
    void handleRequest();
}
