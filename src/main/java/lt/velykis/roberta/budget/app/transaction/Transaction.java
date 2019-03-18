package lt.velykis.roberta.budget.app.transaction;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Transaction {

    private Account account;
    @NotEmpty(message = "Please select account")
    private String accountId;

    private UUID id;

    @NotNull(message = "Please enter date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotEmpty(message = "Please enter description")
    private String description;

    @NotNull(message = "Please enter amount")
    private BigDecimal amount;

}
