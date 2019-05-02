package lt.velykis.roberta.budget.app.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import lt.velykis.roberta.budget.app.account.Account;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Transaction {

    private Account account;
    @NotNull(message = "Please select account")
    private UUID accountId;

    private UUID id;

    @NotNull(message = "Please enter date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotEmpty(message = "Please enter description")
    private String description;

    @NotNull(message = "Please enter amount")
    private BigDecimal amount;

    public Transaction(Account account, UUID id, LocalDate date, String description, BigDecimal amount) {

        this.account = account;
        this.accountId = account == null ? null : account.getId();
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;

    }

}
