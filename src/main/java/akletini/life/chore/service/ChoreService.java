package akletini.life.chore.service;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.shared.EntityService;
import org.springframework.data.domain.Page;

public interface ChoreService extends EntityService<Chore> {

    Page<Chore> getChores(int page, int pageSize);

    Chore completeChore(Chore chore);
}
