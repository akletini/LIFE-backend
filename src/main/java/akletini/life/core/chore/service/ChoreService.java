package akletini.life.core.chore.service;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.BusinessException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public abstract class ChoreService extends EntityService<Chore> {

    public abstract Page<Chore> getChores(int page, int pageSize, Optional<String> sortBy,
                                          Optional<List<String>> filterBy);

    public abstract Chore completeChore(Chore chore) throws BusinessException;
}
