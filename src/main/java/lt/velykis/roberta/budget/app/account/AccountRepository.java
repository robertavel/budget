package lt.velykis.roberta.budget.app.account;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Mapper
public interface AccountRepository {

    List<Account> findAll();

    Account find(UUID id);

    void insert(Account account);

    void update(Account account);

    void delete(UUID id);

    void deleteAll();
}
