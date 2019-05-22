package lt.velykis.roberta.budget.app.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Before
    public void setUp() {
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

        Transaction transaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));

        repository.insert(transaction);
        assertThat(transaction.getId()).isNotNull();

        List<Transaction> transactions = repository.findAll();
        assertThat(transactions).isNotNull();

        assertThat(transactions).containsExactly(transaction);

    }

    @Test
    public void findSingle() {

        Transaction transaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));

        repository.insert(transaction);
        assertThat(transaction.getId()).isNotNull();

        Transaction found = repository.find(transaction.getId());
        assertThat(found).isEqualTo(transaction);

    }

    @Test
    public void update() {

        Transaction transaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));

        repository.insert(transaction);

        Transaction updateTransaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), transaction.getId(),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(140));
        repository.update(updateTransaction);

        Transaction found = repository.find(updateTransaction.getId());
        assertThat(found).isEqualTo(updateTransaction);
    }

    @Test
    public void delete() {

        Transaction transaction = new Transaction(null, UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508088"),
                LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(130));
        repository.insert(transaction);

        repository.delete(transaction.getId());

        List<Transaction> transactions = repository.findAll();
        assertThat(transactions).isEmpty();
    }

}
