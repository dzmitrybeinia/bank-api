package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.account.Account;
import com.dzmitrybeinia.account.AccountService;
import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import com.dzmitrybeinia.user.UserService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CloseAccountProcessor implements OperationProcessor {

    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;

    public CloseAccountProcessor(Scanner scanner, AccountService accountService, UserService userService) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id");
        int accountId = Integer.parseInt(scanner.nextLine());
        Account account = accountService.closeAccount(accountId);
        var user = userService.findUserById(account.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No such user with id %s".formatted(account.getUserId())));
        user.getAccounts().remove(account);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CLOSE;
    }
}
