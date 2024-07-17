package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import com.dzmitrybeinia.user.User;
import com.dzmitrybeinia.user.UserService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateUserProcessor implements OperationProcessor {

    private final Scanner scanner;
    private final UserService userService;

    public CreateUserProcessor(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter login for new user");
        String login = scanner.nextLine();
        User user = userService.createUser(login);
        System.out.println("user created: " + user.toString());
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.USER_CREATE;
    }
}
