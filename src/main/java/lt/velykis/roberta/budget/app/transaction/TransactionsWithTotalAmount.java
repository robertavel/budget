package lt.velykis.roberta.budget.app.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class TransactionsWithTotalAmount {

    private List<Transaction> transactionList;
    private BigDecimal totalAmount;

    public TransactionsWithTotalAmount(List<Transaction> transactionList, BigDecimal totalAmount) {
        this.transactionList = transactionList;
        this.totalAmount = totalAmount;
    }
}
