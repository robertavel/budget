package lt.velykis.roberta.budget.app.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class TransactionsWithTotalAmount {

    private List<Transaction> transactions;
    private BigDecimal totalAmount;

    public TransactionsWithTotalAmount(List<Transaction> transactions, BigDecimal totalAmount) {
        this.transactions = transactions;
        this.totalAmount = totalAmount;
    }
}
