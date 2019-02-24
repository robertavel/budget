package lt.velykis.roberta.budget.app.transaction;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionService {

    public List<Transaction> getAllTransactions() {
        return Arrays.asList(
                new Transaction(LocalDate.of(2019, 1, 15), "First transaction", new BigDecimal(200.00)),
                new Transaction(LocalDate.of(2019, 2, 5), "Second transaction", new BigDecimal(-150.05))
        );
    }

}
