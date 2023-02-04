package akletini.life.todo.repository.api;

import akletini.life.todo.repository.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    @Query("from Tag t where t.name=:tagName")
    Optional<Tag> findByName(@Param("tagName") String tagName);

}
