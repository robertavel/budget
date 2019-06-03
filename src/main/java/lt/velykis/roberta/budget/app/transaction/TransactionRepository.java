package lt.velykis.roberta.budget.app.transaction;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Mapper
public interface TransactionRepository {

    List<Transaction> findAll();

    List<Transaction> findAllWithAccount();

    Transaction find(UUID id);

    List<Transaction> filter(UUID accountId);

    void insert(Transaction transaction);

    void update(Transaction transaction);

    void delete(UUID id);

    void deleteAll();

}
