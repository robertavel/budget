package lt.velykis.roberta.budget.app.transaction;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Mapper
public interface TransactionRepository {

    @Select("SELECT * FROM transactionTable")
    List<Transaction> findAll();

    @Select("SELECT * FROM transactionTable WHERE id = #{id}")
    Transaction findTransaction(UUID id);

    @Select("SELECT * FROM transactionTable WHERE accountId = #{accountId}")
    List<Transaction> filterTransaction(UUID accountId);

    @Select("INSERT INTO TABLE transactionTable(accountId, id, date, description, amount) " +
            "VALUES(accountId = #{accountId}, id = #{id}, date = #{date}, description = #{description}, amount = #{amount})")
    void insert(Transaction transaction);


    @Select("UPDATE TABLE accountTable SET accountId = #{accountId}, id = #{id}, date = #{date}, description = #{description}, amount = #{amount}")
    public void update(Transaction transaction);

    @Select("DELETE FROM transactionTable WHERE id=#{id}")
    void delete(UUID id);

}
