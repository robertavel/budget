package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
