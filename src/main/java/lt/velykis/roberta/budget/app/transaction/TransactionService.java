package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import lt.velykis.roberta.budget.app.account.AccountRepository;
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
    private final AccountRepository accountRepository;


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getAllTransactions() {
        return FunctionUtil.toList(transactionRepository.findAll());
    }

    public List<Account> getAllAccounts() {
        return FunctionUtil.toList(accountRepository.findAll());
    }

    public void addNewTransaction(Transaction transaction) {
        transactionRepository.insert(transaction);
    }

    public Optional<Account> findAccount(UUID id) {
        return Optional.ofNullable(accountRepository.findAccount(id));
    }

    public Optional<Transaction> findTransaction(UUID id) {
        return Optional.ofNullable(transactionRepository.findTransaction(id));
    }

    public List<Transaction> filterTransaction(UUID accountId) {
        return transactionRepository.filterTransaction(accountId);
    }

    public void updateTransaction(Account account, UUID id, Transaction updatedTransaction) {

        Transaction newTransaction = new Transaction(
                account,
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
