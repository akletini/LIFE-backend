package akletini.life.chore.service;

import akletini.life.chore.repository.entity.Chore;
import org.springframework.data.domain.Page;

public interface ChoreService {

    Page<Chore> getChores(int page, int pageSize);

    Chore store(Chore chore);
}
