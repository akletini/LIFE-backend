package akletini.life.core.todo.repository.api;

import akletini.life.core.task.entity.TaskRepository;
import akletini.life.core.todo.repository.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends TaskRepository<Todo> {

    @Query(value = """
            SELECT t FROM Todo t WHERE
            (:open is null or t.state=:open) and
            (cast(:due as date) is null or t.dueAt<=:due) and
            (:done is null or t.state=:done) and
            (:#{#tags == null} = true or (t.tag.id in (:tags)))
            """)
    Page<Todo> findFiltered(Pageable pageable, @Param("open") Todo.State open,
                            @Param("due") LocalDate due, @Param("done") Todo.State done,
                            @Param("tags") List<Long> tags);
}
