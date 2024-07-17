package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.account.AccountService;
import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TransferAccountProcessor implements OperationProcessor {

    private final Scanner scanner;
    private final AccountService accountService;

    public TransferAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter source account id");
        int sourceAccountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter destination account id");
        int destinationAccountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to withdraw");
        int amount = Integer.parseInt(scanner.nextLine());
        accountService.transfer(sourceAccountId, destinationAccountId, amount);
        System.out.printf(
                "Successfully transferred from acc %s to acc %s amount = %s%n",
                sourceAccountId, destinationAccountId, amount
        );
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_TRANSFER;
    }
}
