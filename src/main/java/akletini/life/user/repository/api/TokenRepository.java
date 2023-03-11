package akletini.life.user.repository.api;

import akletini.life.user.repository.entity.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends ListCrudRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);

    @Modifying
    @Query(value = """
            delete from Token t where t.expired = true or t.revoked = true
            """)
    void deleteExpiredTokens();
    Optional<Token> findByToken(String token);
}
