package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import lt.velykis.roberta.budget.app.account.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    public void findAll_empty() {

        List<Transaction> transactions = repository.findAll();
        assertThat(transactions).isNotNull();
        assertThat(transactions).isEmpty();
    }

    @Test
    public void findAll_single() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        accountRepository.insert(account);

        Transaction transaction = new Transaction(account, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));

        repository.insert(transaction);
        assertThat(transaction.getId()).isNotNull();

        List<Transaction> transactions = repository.findAll();
        assertThat(transactions).isNotNull();

        assertThat(transactions).containsExactly(transaction);

    }

    @Test
    public void findAllWithAcc() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        accountRepository.insert(account);

        Transaction transaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));
        repository.insert(transaction);
        assertThat(transaction.getId()).isNotNull();

        Transaction transactionWithAcc = new Transaction(account, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));

        List<Transaction> transactionsWithAccount = repository.findAllWithAccount();
        Transaction found = repository.find(transaction.getId());
        assertThat(transactionsWithAccount).containsExactly(transactionWithAcc);
    }

    @Test
    public void findSingle() {
        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        accountRepository.insert(account);
        Account account2 = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508099"), "Andriaus SEB");
        accountRepository.insert(account2);

        Transaction transaction = new Transaction(account, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));
        repository.insert(transaction);
        assertThat(transaction.getId()).isNotNull();

        Transaction found = repository.find(transaction.getId());
        assertThat(found).isEqualTo(transaction);

    }

    @Test
    public void filterTransactions() {

        Account account1 = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        accountRepository.insert(account1);

        Account account2 = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508099"), "Andriaus SEB");
        accountRepository.insert(account2);

        Transaction transaction1 = new Transaction(account1, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));
        repository.insert(transaction1);

        Transaction transaction2 = new Transaction(account2, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508099"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508055"),
                LocalDate.of(2019, 1, 16), "Second transaction", new BigDecimal(140));
        repository.insert(transaction2);

        List<Transaction> filterTransactions = repository.filter(account1.getId());
        List<Transaction> expectedTransaction = new ArrayList<>();
        expectedTransaction.add(transaction1);
        assertThat(filterTransactions).isEqualTo(expectedTransaction);


    }

    @Test
    public void update() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        accountRepository.insert(account);

        Transaction transaction = new Transaction(account, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));

        repository.insert(transaction);

        Transaction updateTransaction = new Transaction(account, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), transaction.getId(),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(140));
        repository.update(updateTransaction);

        Transaction found = repository.find(updateTransaction.getId());
        assertThat(found).isEqualTo(updateTransaction);
    }

    @Test
    public void delete() {
        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        accountRepository.insert(account);

        Transaction transaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));
        repository.insert(transaction);

        repository.delete(transaction.getId());

        List<Transaction> transactions = repository.findAll();
        assertThat(transactions).isEmpty();
    }

}
