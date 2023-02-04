package akletini.life.user.repository.api;

import akletini.life.user.repository.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("from User u where u.email=:email")
    Optional<User> findByEmail(@Param("email") String email);
}
