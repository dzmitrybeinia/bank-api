package com.dzmitrybeinia.user;

import com.dzmitrybeinia.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final Map<Integer, User> users;
    private final Set<String> logins;
    private final AtomicInteger idCounter;

    private final AccountService accountService;

    @Autowired
    public UserService(AccountService accountService) {
        this.accountService = accountService;
        this.logins = new HashSet<>();
        this.users = new HashMap<>();
        this.idCounter = new AtomicInteger(0);
    }

    public User createUser(final String login) {
        if(logins.contains(login)) {
            throw new IllegalArgumentException("User already exists with login = %s".formatted(login));
        }
        logins.add(login);
        var user = new User(idCounter.incrementAndGet(), login, new ArrayList<>());

        var defaultAccount = accountService.createAccount(user);
        user.getAccounts().add(defaultAccount);

        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findUserById(final int id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }
}
