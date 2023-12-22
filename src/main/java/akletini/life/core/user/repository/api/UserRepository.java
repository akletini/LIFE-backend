package akletini.life.core.user.repository.api;

import akletini.life.core.user.repository.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {

    @Query("from User u where u.email=:email")
    Optional<User> findByEmail(@Param("email") String email);
}
