package lt.velykis.roberta.budget.app.transaction;

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

    @PostMapping("/transactions")
    public String addTransaction(@Valid Transaction transaction,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "transaction/add";
        }

        transaction.setId(UUID.randomUUID());

        transactionService.addNewTransaction(transaction);

        return listAllTransactions(model);
    }

    @GetMapping("/add")
    public String showAddTransactionForm(Model model) {

        Transaction transaction = new Transaction(null, null, null, null);

        model.addAttribute("transaction", transaction);

        return "transaction/add";
    }

    @GetMapping("/transactions/{transactionId}")
    public String showEditTransactionForm(@PathVariable("transactionId") UUID transactionId,
                                          Model model) {

        Optional<Transaction> transaction = transactionService.findTransaction(transactionId);

        if (!transaction.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }

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

        transactionService.updateTransaction(transactionId, updatedTransaction);

        return "redirect:/transactions";
    }

    @PostMapping(value = "/transactions/{transactionId}", params = "delete=true")
    public String deleteTransaction(@PathVariable("transactionId") UUID transactionId) {

        transactionService.deleteTransaction(transactionId);

        return "redirect:/transactions";
    }


}
