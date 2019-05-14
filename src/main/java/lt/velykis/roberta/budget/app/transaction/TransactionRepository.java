package lt.velykis.roberta.budget.app.transaction;

import org.apache.ibatis.annotations.*;
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

    @Insert("INSERT INTO TABLE transactionTable(accountId, id, date, description, amount) " +
            "VALUES(#{accountId}, #{id}, #{date}, #{description}, #{amount})")
    void insert(Transaction transaction);

    @Update("UPDATE TABLE transactionTable SET accountId = #{accountId}, date = #{date}, description = #{description}, amount = #{amount}" +
            "WHERE id = #{id}")
    void update(Transaction transaction);

    @Delete("DELETE FROM transactionTable WHERE id=#{id}")
    void delete(UUID id);

    @Delete("DELETE FROM transactionTable")
    void deleteAll();

}
