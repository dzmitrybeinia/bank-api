package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.account.AccountService;
import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class WithdrawAccountProcessor implements OperationProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public WithdrawAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to withdraw");
        int amount = Integer.parseInt(scanner.nextLine());
        accountService.withdraw(accountId, amount);
        System.out.printf("Successfully withdraw amount %s from account %s%n", amount, accountId);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_WITHDRAW;
    }
}
