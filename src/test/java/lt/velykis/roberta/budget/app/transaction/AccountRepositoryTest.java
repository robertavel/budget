package lt.velykis.roberta.budget.app.transaction;

import lt.velykis.roberta.budget.app.account.Account;
import lt.velykis.roberta.budget.app.account.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void findAll_empty() {

        List<Account> accounts = repository.findAll();
        assertThat(accounts).isNotNull();
        assertThat(accounts).isEmpty();
    }

    @Test
    public void findAll_single() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");

        repository.insert(account);
        assertThat(account.getId()).isNotNull();

        List<Account> accounts = repository.findAll();
        assertThat(accounts).isNotNull();
        assertThat(accounts).containsExactly(account);
    }

    @Test
    public void findSingle() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");

        repository.insert(account);
        assertThat(account.getId()).isNotNull();

        Account found = repository.find(account.getId());
        assertThat(found).isEqualTo(account);
    }

    @Test
    public void update() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");
        repository.insert(account);

        Account updatedAccount = new Account(account.getId(), "Andriaus SEB");
        repository.update(updatedAccount);

        Account found = repository.find(updatedAccount.getId());
        assertThat(found).isEqualTo(updatedAccount);
    }

    @Test
    public void delete() {

        Account account = new Account(UUID.fromString("15c83e74-ebb1-4bb0-a2b7-83da2d508098"), "Robertos SEB");

        repository.insert(account);
        repository.delete(account.getId());

        List<Account> accounts = repository.findAll();
        assertThat(accounts).isEmpty();
    }

}
