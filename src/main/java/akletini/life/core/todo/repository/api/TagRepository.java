package akletini.life.core.todo.repository.api;

import akletini.life.core.todo.repository.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends ListCrudRepository<Tag, Long> {

    @Query("from Tag t where t.name=:tagName")
    Optional<Tag> findByName(@Param("tagName") String tagName);

}
