package lt.velykis.roberta.budget.app.account;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Mapper
public interface AccountRepository {

    @Select("SELECT * FROM accountTable")
    List<Account> findAll();

    @Select("SELECT * FROM accountTable WHERE id = #{id}")
    Account find(UUID id);

    @Insert("INSERT INTO accountTable(id, name) VALUES(#{id}, #{name})")
    void insert(Account account);

    @Update("UPDATE accountTable SET id = #{id}, name = #{name} WHERE id = #{id}")
    void update(Account account);

    @Delete("DELETE FROM accountTable WHERE id = #{id}")
    void delete(UUID id);

    @Delete("DELETE FROM accountTable")
    void deleteAll();
}
