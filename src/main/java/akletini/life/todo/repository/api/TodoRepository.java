package akletini.life.todo.repository.api;

import akletini.life.todo.repository.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static akletini.life.shared.constants.FilterConstants.*;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long>,
        PagingAndSortingRepository<Todo, Long> {

    @Query(value = """
            SELECT t FROM Todo t WHERE
            (:open is null or t.state=:open) and
            (cast(:dueAt as date) is null or t.dueAt>=:dueAt) and
            (:done is null or t.state=:done)
            """)
    Page<Todo> findFiltered(Pageable pageable, @Param(OPEN) String open,
                            @Param(DUE) Date due, @Param(DONE) String done);
}
