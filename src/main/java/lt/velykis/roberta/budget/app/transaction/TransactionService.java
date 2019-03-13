package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return FunctionUtil.toList(transactionRepository.findAll());
    }

    public void addNewTransaction(Transaction transaction) {
        transactionRepository.insert(transaction);
    }

    public Optional<Transaction> findTransaction(UUID id) {
        return transactionRepository.findTransaction(id);
    }

    public void updateTransaction(UUID id, Transaction updatedTransaction) {

        Transaction newTransaction = new Transaction(
                id,
                updatedTransaction.getDate(),
                updatedTransaction.getDescription(),
                updatedTransaction.getAmount());

        transactionRepository.update(newTransaction);

    }

    public void deleteTransaction(UUID id) {

        transactionRepository.delete(id);

    }

    public static BigDecimal countTotal(List<Transaction> transactions) {

        BigDecimal sum = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            sum = sum.add(transaction.getAmount());
        }

        return sum;

    }
}
