package akletini.life.task.entity;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface TaskRepository<T extends Task> extends ListCrudRepository<T, Long>,
        ListPagingAndSortingRepository<T, Long> {
}
