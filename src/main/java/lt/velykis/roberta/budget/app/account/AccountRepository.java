package lt.velykis.roberta.budget.app.account;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Mapper
public interface AccountRepository {

    @Select("SELECT * FROM accountTable")
    List<Account> findAll();

    @Select("SELECT * FROM accountTable WHERE id = #{id}")
    Account findAccount(UUID id);

    @Select("INSERT INTO TABLE accountTable(id, name) VALUES(id = #{id}, name=#{name})")
    void insert(Account account);

    @Select("UPDATE TABLE accountTable SET id = #{id}, name=#{name})")
    void update(Account account);

    @Select("DELETE FROM accountTable WHERE id=#{id}")
    void delete(UUID id);

}
