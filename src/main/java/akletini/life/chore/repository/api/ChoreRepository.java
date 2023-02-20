package akletini.life.chore.repository.api;

import akletini.life.chore.repository.entity.Chore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChoreRepository extends CrudRepository<Chore, Long>, PagingAndSortingRepository<Chore, Long> {
}
