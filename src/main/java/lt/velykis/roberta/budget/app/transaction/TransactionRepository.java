package lt.velykis.roberta.budget.app.transaction;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionRepository() {
        initDefaultData();
    }

    private void initDefaultData() {
        Stream.of(
                new Transaction(LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(200.00)),
                new Transaction(LocalDate.of(2019, 2, 5), "Second transaction", new BigDecimal(-150.05))
        ).forEach(transactions::add);
    }

    public Iterable<Transaction> findAll() {
        return transactions;
    }

}
