package lt.velykis.roberta.budget.app.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
        model.addAttribute("transactions", transactions);

        return "transaction/transactions";
    }

    @PostMapping("/transactions")
    public String addTransaction(Transaction transaction, Model model) {

        transactionService.addNewTransaction(transaction);

        return listAllTransactions(model);
    }

    @GetMapping("/add")
    public String addTransactionForm(Model model) {

        Transaction transaction = new Transaction(null, null, null);

        model.addAttribute("transaction", transaction);

        return "transaction/add";
    }


}
