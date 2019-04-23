package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionRepository() {
        initDefaultData();
    }

    private void initDefaultData() {
        Stream.of(
                new Transaction(
                        new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"), "Andriaus SEB"),
                        UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"),
                        LocalDate.of(2019, 1, 15),
                        "First transaction",
                        new BigDecimal("200.00")),
                new Transaction(
                        new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"), "Robertos SEB"),
                        UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"),
                        LocalDate.of(2019, 2, 5),
                        "Second transaction",
                        new BigDecimal("-150.05"))
        ).forEach(transactions::add);
    }

    public Iterable<Transaction> findAll() {
        return transactions;
    }

    public Optional<Transaction> findTransaction(UUID id) {
        return transactions.stream()
                .filter(tr -> id.equals(tr.getId()))
                .findFirst();
    }

    public List<Transaction> filterTransaction(UUID accountId) {
        return transactions.stream()
                .filter(tr -> accountId.equals(tr.getAccountId()))
                .collect(Collectors.toList());
    }

    public void insert(Transaction transaction) {
        transactions.add(transaction);
    }

    public void update(Transaction transaction) {
        transactions.replaceAll(tr ->
                tr.getId().equals(transaction.getId()) ? transaction : tr);
    }

    public void delete(UUID id) {
        transactions.removeIf(tr -> id.equals(tr.getId()));
    }

}
