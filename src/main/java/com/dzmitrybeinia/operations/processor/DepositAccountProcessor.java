package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.account.AccountService;
import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DepositAccountProcessor implements OperationProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public DepositAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to deposit");
        int amount = Integer.parseInt(scanner.nextLine());
        accountService.depositAccount(accountId, amount);
        System.out.printf("Successfully deposited amount %s to account %s%n", amount, accountId);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_DEPOSIT;
    }
}
