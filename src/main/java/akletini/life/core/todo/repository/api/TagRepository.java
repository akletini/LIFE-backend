package akletini.life.core.todo.repository.api;

import akletini.life.core.todo.repository.entity.Tag;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends ListCrudRepository<Tag, Long>,
        ListPagingAndSortingRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
