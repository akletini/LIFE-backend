package akletini.life.chore.repository.api;

import akletini.life.chore.repository.entity.Chore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ChoreRepository extends CrudRepository<Chore, Long>,
        PagingAndSortingRepository<Chore, Long> {

    @Query(value = """
            SELECT c FROM Chore c WHERE
             (:active is null or c.active=:active) and
             (cast(:dueAt as date) is null or c.dueAt<=:dueAt)""")
    Page<Chore> findFiltered(Pageable pageable, @Param("active") Boolean isActive,
                             @Param("dueAt") LocalDate dueAt);

}
