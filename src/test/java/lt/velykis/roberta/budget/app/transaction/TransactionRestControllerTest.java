package lt.velykis.roberta.budget.app.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.velykis.roberta.budget.app.account.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionRestController.class)
public class TransactionRestControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    private Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"), "Robertos SEB");

    private Transaction transaction1 = new Transaction(new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"), "Robertos SEB"),
            UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"),
            UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"),
            LocalDate.of(2019, 1, 15),
            "First transaction",
            new BigDecimal(2));

    private Transaction transaction2 = new Transaction(new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508006"), "Robertos SEB"),
            UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508006"),
            UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508007"),
            LocalDate.of(2020, 2, 16),
            "Second transaction",
            new BigDecimal(2));

    private Transaction transactionNotValid = new Transaction(new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508006"), "Robertos SEB"),
            UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508006"),
            null,
            LocalDate.of(2020, 2, 16),
            null,
            new BigDecimal(2));

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper()
                    .findAndRegisterModules()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    public void test_cors_headers() throws Exception {
//        mockMvc.perform(get("/rest/transactions"))
//                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
//                .andExpect(header().string("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"));
//                .andExpect(header().string("Access-Control-Allow-Headers", "*"));
//                .andExpect(header().string("Access-Control-Max-Age", "3600"));
//    }


    @Test
    public void listTransactions_empty_zeroTotal() throws Exception {

        when(transactionService.getAllTransactions())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rest/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"transactions\":[],\"totalAmount\":0}", true))
                .andDo(print());
    }

    @Test
    public void listTransactions_single() throws Exception {

        when(transactionService.getAllTransactions())
                .thenReturn(Collections.singletonList(transaction1));

        String expectedOutput = loadFile("transaction/rest/all-transactions-single.json");

        mockMvc.perform(get("/rest/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount", is(2)))
                .andExpect(jsonPath("$.transactions", hasSize(1)))
                .andExpect(jsonPath("$.transactions[0].account.id", is("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .andExpect(content().json(expectedOutput, true))
                .andDo(print());
//        verifyNoMoreInteractions(transactionService);

    }

    private String loadFile(String path) throws URISyntaxException, IOException {
        Path p = Paths.get(getClass().getClassLoader().getResource(path).toURI());
        byte[] fileBytes = Files.readAllBytes(p);
        return new String(fileBytes);
    }

    @Test
    public void listTransactions() throws Exception {

        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(get("/rest/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", hasSize(2)))
                .andExpect(jsonPath("$.transactions[0].account.id", is("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .andExpect(jsonPath("$.transactions[0].id", is("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .andExpect(jsonPath("$.transactions[0].date", is("2019-01-15")))
                .andExpect(jsonPath("$.transactions[0].description", is("First transaction")))
                .andExpect(jsonPath("$.transactions[0].amount", is(2)))
                .andExpect(jsonPath("$.transactions[1].account.id", is("15c83e74-ebb1-4bb0-a2b7-83da2d508006")))
                .andExpect(jsonPath("$.transactions[1].id", is("15c83e74-ebb1-4bb0-a2b7-83da2d508007")))
                .andExpect(jsonPath("$.transactions[1].date", is("2020-02-16")))
                .andExpect(jsonPath("$.transactions[1].description", is("Second transaction")))
                .andExpect(jsonPath("$.transactions[1].amount", is(2)));
    }

    @Test
    public void listTransactions_totalAmount() throws Exception {

        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(get("/rest/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount", is(4)));
    }


    @Test
    public void findTransaction() throws Exception {

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.of(transaction1));

        String expectedOutput = loadFile("transaction/rest/transaction1.json");

        mockMvc.perform(get("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedOutput, true));

    }

    @Test
    public void findTransactionFail404NotFound() throws Exception {

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addTransaction() throws Exception {

        doNothing().when(transactionService).addNewTransaction(transaction1);

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .thenReturn(Optional.of(account));

        mockMvc.perform(post("/rest/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transaction1)))
                .andExpect(status().isOk());


        verify(transactionService, times(1)).findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"));
//        verify(transactionService, times(1)).addNewTransaction(transaction1);
    }

    @Test
    public void addTransactionFail400BadRequest() throws Exception {

        doNothing().when(transactionService).addNewTransaction(transactionNotValid);

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508006")))
                .thenReturn(Optional.of(account));

        mockMvc.perform(post("/rest/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transactionNotValid)))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void addTransactionFail404NotFoundAccount() throws Exception {

        doNothing().when(transactionService).addNewTransaction(transaction1);

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/rest/transactions")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transaction1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void editTransaction() throws Exception {

        doNothing().when(transactionService).updateTransaction(
                account,
                UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"),
                transaction1);

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.of(transaction1));

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .thenReturn(Optional.of(account));

        mockMvc.perform(put("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transaction1)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(transactionService, times(1)).findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"));
        verify(transactionService, times(1)).findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004"));
        verify(transactionService, times(1)).updateTransaction(
                account,
                UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"),
                transaction1);
        verifyNoMoreInteractions(transactionService);

    }

    @Test
    public void editTransactionNotValidTransaction() throws Exception {

        doNothing().when(transactionService).updateTransaction(
                account,
                UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"),
                transactionNotValid);

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.of(transactionNotValid));

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .thenReturn(Optional.of(account));

        mockMvc.perform(put("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transactionNotValid)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void editTransactionFail404NotFoundTransaction() throws Exception {

        doNothing().when(transactionService).updateTransaction(
                account,
                UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508015"),
                transaction1);

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508015")))
                .thenReturn(Optional.empty());

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .thenReturn(Optional.of(account));

        mockMvc.perform(put("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508015")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transaction1)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void editTransactionFail404NotFoundAccount() throws Exception {

        doNothing().when(transactionService).updateTransaction(
                account,
                UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"),
                transaction1);

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.of(transaction1));

        when(transactionService.findAccount(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508004")))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(transaction1)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


    @Test
    public void deleteTransaction() throws Exception {

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.of(transaction1));

        mockMvc.perform(delete("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(transactionService).deleteTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005"));

    }

    @Test
    public void deleteTransaction_withException() throws Exception {

        when(transactionService.findTransaction(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508005")))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/rest/transactions/15c83e74-ebb1-4bb0-a2b7-83da2d508005"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
