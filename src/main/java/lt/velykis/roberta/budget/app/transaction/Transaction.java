package lt.velykis.roberta.budget.app.transaction;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class Transaction {

    private final LocalDate date;
    private final String description;
    private final BigDecimal amount;

}
