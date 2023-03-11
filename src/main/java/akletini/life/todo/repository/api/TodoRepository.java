package akletini.life.todo.repository.api;

import akletini.life.task.entity.TaskRepository;
import akletini.life.todo.repository.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static akletini.life.shared.constants.FilterConstants.*;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long>,
        PagingAndSortingRepository<Todo, Long>, TaskRepository<Todo> {

    @Query(value = """
            SELECT t FROM Todo t WHERE
            (:open is null or t.state=:open) and
            (cast(:due as date) is null or t.dueAt<=:due) and
            (:done is null or t.state=:done) and
            (:#{#tags == null} = true or (t.tag.id in (:tags))) and
            t.assignedUser.id=:userId
            """)
    Page<Todo> findFiltered(Pageable pageable, @Param(OPEN) Todo.State open,
                            @Param(DUE) Date due, @Param(DONE) Todo.State done,
                            @Param("tags") List<Long> tags, Long userId);
}
