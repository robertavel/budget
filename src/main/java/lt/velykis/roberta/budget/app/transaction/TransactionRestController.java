package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/rest/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public TransactionsWithTotalAmount listAllTransactions(@RequestParam(required = false, name = "accountId") Optional<UUID> accountId) {

        List<Transaction> transactions = accountId.map(transactionService::filterTransaction)
                .orElseGet(transactionService::getAllTransactions);

        BigDecimal totalAmount = TransactionService.countTotal(transactions);
        return new TransactionsWithTotalAmount(transactions, totalAmount);

    }

    @GetMapping("/{transactionId}")
    public Optional<Transaction> transaction(@PathVariable("transactionId") UUID transactionId) {

        Optional<Transaction> transaction = transactionService.findTransaction(transactionId);

        if (!transaction.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

        return transactionService.findTransaction(transactionId);
    }

    @PostMapping("")
    public List<Transaction> addTransaction(@Valid @RequestBody Transaction transaction,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.valueOf(bindingResult.getAllErrors()));
        }

        Optional<Account> account = transactionService.findAccount(transaction.getAccountId());

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        transaction.setId(UUID.randomUUID());
        transactionService.addNewTransaction(transaction);

        return transactionService.getAllTransactions();
    }

    @PutMapping("/{transactionId}")
    public Transaction editTransaction(@PathVariable("transactionId") UUID transactionId,
                                       @Valid @RequestBody Transaction updatedTransaction,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.valueOf(bindingResult.getAllErrors()));
        }

        Optional<Transaction> transaction = transactionService.findTransaction(transactionId);

        if (!transaction.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

        Optional<Account> account = transactionService.findAccount(updatedTransaction.getAccountId());

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        transactionService.updateTransaction(account.get(), transactionId, updatedTransaction);

        return updatedTransaction;
    }

    @DeleteMapping(value = "/{transactionId}")
    public void deleteTransaction(@PathVariable("transactionId") UUID transactionId) {

        Optional<Transaction> transaction = transactionService.findTransaction(transactionId);

        if (!transaction.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

        transactionService.deleteTransaction(transactionId);
    }
}
