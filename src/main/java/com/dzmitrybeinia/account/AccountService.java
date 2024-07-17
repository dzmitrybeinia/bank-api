package com.dzmitrybeinia.account;

import com.dzmitrybeinia.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountService {

    private final Map<Integer, Account> accounts;
    private final AtomicInteger idCounter;

    private final int defaultAmount;
    private final double transferCommission;

    @Autowired
    public AccountService(@Value("${account.default:500}") int defaultAmount,
                          @Value("${account.transfer-commission:0.01}") double transferCommission) {
        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
        this.accounts = new HashMap<>();
        this.idCounter = new AtomicInteger(0);
    }

    public Account createAccount(final User user) {
        var account = new Account(idCounter.incrementAndGet(), user.getId(), defaultAmount);
        accounts.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findAccountById(final int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public List<Account> getAllUserAccounts(final int userId) {
        return accounts.values().stream()
                .filter(account -> account.getUserId() == userId)
                .toList();
    }

    public void depositAccount(final int accountId, int moneyToDeposit) {
        var account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found = %s".formatted(accountId)));
        if(moneyToDeposit <= 0) {
            throw new IllegalArgumentException("Cannot deposit negative amount = '%s".formatted(moneyToDeposit));
        }
        int money = account.getMoneyAmount() + moneyToDeposit;
        account.setMoneyAmount(money);
    }

    public void withdraw(int accountId, int amount) {
        var account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found = %s".formatted(accountId)));
        if(amount <= 0) {
            throw new IllegalArgumentException("Cannot withdraw negative amount");
        }
        if(account.getMoneyAmount() < amount) {
            throw new IllegalArgumentException(("Cannot withdraw from amount = '%s, " +
                    "money amount = %s, attempted withdraw = %s").formatted(account.getId(), account.getMoneyAmount(),
                    amount));
        }
        int money = account.getMoneyAmount() - amount;
        account.setMoneyAmount(money);
    }

    public Account closeAccount(int accountId) {
        var accountToDelete = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found = %s".formatted(accountId)));
        var accounts = getAllUserAccounts(accountToDelete.getUserId());
        if(accounts.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one account");
        }
        Account accToDeposit = accounts.stream()
                .filter(a -> a.getId() != accountId).findFirst().orElseThrow();
        accToDeposit.setMoneyAmount(accToDeposit.getMoneyAmount() + accountToDelete.getMoneyAmount());
        accounts.remove(accountId);
        return accountToDelete;
    }

    public void transfer(int sourceAccountId, int destinationAccountId, int amount) {
        var from = findAccountById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found = %s".formatted(sourceAccountId)));
        var to = findAccountById(destinationAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found = %s".formatted(destinationAccountId)));
        if(amount <= 0) {
            throw new IllegalArgumentException("Cannot withdraw negative amount");
        }
        if(from.getMoneyAmount() < amount) {
            throw new IllegalArgumentException(("Cannot withdraw from amount = '%s, " +
                    "money amount = %s, attempted withdraw = %s").formatted(sourceAccountId, from.getMoneyAmount(),
                    amount));
        }
        from.setMoneyAmount(from.getMoneyAmount() - amount);
        to.setMoneyAmount(from.getUserId() == to.getUserId() ? to.getMoneyAmount() + amount :
                (int) (to.getMoneyAmount() + amount * (1 - transferCommission)));
    }
}
