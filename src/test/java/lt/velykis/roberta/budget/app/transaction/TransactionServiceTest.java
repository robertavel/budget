package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionServiceTest {

    @Test
    public void total_empty_is_zero() {

        List<Transaction> transactions = emptyList();

        BigDecimal result = TransactionService.countTotal(transactions);

        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void total_two_positive_amounts_are_added() {

        List<Transaction> transactions = Arrays.asList(
                transaction("10"),
                transaction("20")
        );

        BigDecimal result = TransactionService.countTotal(transactions);

        assertThat(result).isEqualTo(new BigDecimal("30"));
    }

    @Test
    public void total_negative_and_positive_amounts_are_added() {

        List<Transaction> transactions = Arrays.asList(
                transaction("-10"),
                transaction("20.05")
        );

        BigDecimal result = TransactionService.countTotal(transactions);

        assertThat(result).isEqualTo(new BigDecimal("10.05"));
    }

    private static Transaction transaction(String amount) {

        return new Transaction(
                new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"), "Andriaus SEB"),
                UUID.randomUUID(),
                LocalDate.of(2019, 1, 15),
                "First transaction",
                new BigDecimal(amount));
    }
}
