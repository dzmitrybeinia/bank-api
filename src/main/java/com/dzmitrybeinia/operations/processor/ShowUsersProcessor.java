package com.dzmitrybeinia.operations.processor;

import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import com.dzmitrybeinia.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class ShowUsersProcessor implements OperationProcessor {
    private final UserService userService;

    public ShowUsersProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        var users = userService.getAllUsers();
        System.out.println("All users:");
        users.forEach(System.out::println);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.SHOW_ALL_USERS;
    }
}
