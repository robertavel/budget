package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public String listAllTransactions(Model model) {

        List<Transaction> transactions = transactionService.getAllTransactions();
        BigDecimal totalAmount = TransactionService.countTotal(transactions);
        model.addAttribute("transactions", transactions);
        model.addAttribute("totalAmount", totalAmount);
        return "transaction/transactions";
    }

    @PostMapping("/add")
    public String addTransaction(@Valid Transaction transaction,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return showAddTransactionForm(transaction, model);
        }

        Optional<Account> account = transactionService.findAccount(UUID.fromString(transaction.getAccountId()));

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        transaction.setAccount(account.get());


        transaction.setId(UUID.randomUUID());

        transactionService.addNewTransaction(transaction);

        return "redirect:/transactions";
    }

    @GetMapping("/add")
    public String showAddTransactionForm(Model model) {

        Transaction transaction = new Transaction(null, null, null, null, null);
        return showAddTransactionForm(transaction, model);
    }

    private String showAddTransactionForm(Transaction transaction, Model model) {

        model.addAttribute("transaction", transaction);

        List<Account> accounts = transactionService.getAllAccounts();
        model.addAttribute("accounts", accounts);

        return "transaction/add";
    }

    @GetMapping("/transactions/{transactionId}")
    public String showEditTransactionForm(@PathVariable("transactionId") UUID transactionId,
                                          Model model) {

        Optional<Transaction> transaction = transactionService.findTransaction(transactionId);

        if (!transaction.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

        List<Account> accounts = transactionService.getAllAccounts();
        model.addAttribute("accounts", accounts);

        model.addAttribute("transaction", transaction.get());

        return "transaction/edit";
    }

    @PostMapping(value = "/transactions/{transactionId}", params = "!delete")
    public String editTransaction(@PathVariable("transactionId") UUID transactionId,
                                  @Valid Transaction updatedTransaction,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "transaction/edit";
        }

        Optional<Transaction> transaction = transactionService.findTransaction(transactionId);

        if (!transaction.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

        Optional<Account> account = transactionService.findAccount(UUID.fromString(updatedTransaction.getAccountId()));

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }


        transactionService.updateTransaction(account.get(), transactionId, updatedTransaction);

        return "redirect:/transactions";
    }

    @PostMapping(value = "/transactions/{transactionId}", params = "delete=true")
    public String deleteTransaction(@PathVariable("transactionId") UUID transactionId) {

        transactionService.deleteTransaction(transactionId);

        return "redirect:/transactions";
    }


}
