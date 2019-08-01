package lt.velykis.roberta.budget.app.account;

import lt.velykis.roberta.budget.app.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final TransactionService transactionService;

    @Autowired
    public AccountController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public List<Account> listAllAccounts() {
        return transactionService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Optional<Account> account(@PathVariable("id") UUID id) {


        Optional<Account> account = transactionService.findAccount(id);

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        return account;
    }

    @PutMapping("/{id}")
    public Account editAccount(@PathVariable("id") UUID id,
                               @Valid @RequestBody Account updatedAccount,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.valueOf(bindingResult.getAllErrors()));
        }

        Optional<Account> account = transactionService.findAccount(id);

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        transactionService.updateAccount(id, updatedAccount);

        return updatedAccount;
    }

    @PostMapping("")
    public Account addAccount(@Valid @RequestBody Account account, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.valueOf(bindingResult.getAllErrors()));
        }

        account.setId(UUID.randomUUID());
        transactionService.addNewAccount(account);

        return account;
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") UUID id) {

        Optional<Account> account = transactionService.findAccount(id);

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        transactionService.deleteAccount(id);
    }
}
