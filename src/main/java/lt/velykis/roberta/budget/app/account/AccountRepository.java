package lt.velykis.roberta.budget.app.account;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class AccountRepository {

    public static final Account ANDRIAUS_SEB = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"), "Andriaus SEB");
    public static final Account ROBERTOS_SEB = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"), "Robertos SEB");

    private final List<Account> accounts = new ArrayList<>();

    public AccountRepository() {
        initDefaultData();
    }

    private void initDefaultData() {
        Stream.of(
                ANDRIAUS_SEB,
                ROBERTOS_SEB,
                new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508006"), "Andriaus pinigine"),
                new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508007"), "Robertos pinigine")
        ).forEach(accounts::add);
    }

    public Iterable<Account> findAll() {
        return accounts;
    }

    public Optional<Account> findAccount(UUID id) {
        return accounts.stream()
                .filter(acc -> id.equals(acc.getId()))
                .findFirst();
    }

    public void insert(Account account) {
        accounts.add(account);
    }

    public void update(Account account) {
        accounts.replaceAll(acc ->
                acc.getId().equals(account.getId()) ? account : acc);
    }

    public void delete(UUID id) {
        accounts.removeIf(acc -> id.equals(acc.getId()));
    }

}
