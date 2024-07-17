package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import com.dzmitrybeinia.account.AccountService;
import com.dzmitrybeinia.user.UserService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateAccountProcessor implements OperationProcessor {
    private final Scanner scanner;
    private final UserService userService;
    private final AccountService accountService;

    public CreateAccountProcessor(Scanner scanner, UserService userService, AccountService accountService) {
        this.scanner = scanner;
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter user id");
        int userId = scanner.nextInt();
        var user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user with id %s".formatted(userId)));
        var account = accountService.createAccount(user);
        user.getAccounts().add(account);
        System.out.println("account created = " + account.toString());
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
