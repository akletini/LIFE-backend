package akletini.life.core.chore.service;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.EntityService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ChoreService extends EntityService<Chore> {

    Page<Chore> getChores(int page, int pageSize, Optional<String> sortBy,
                          Optional<List<String>> filterBy);

    Chore completeChore(Chore chore);
}
