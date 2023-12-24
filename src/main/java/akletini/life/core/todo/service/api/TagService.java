package akletini.life.core.todo.service.api;

import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.todo.repository.entity.Tag;

import java.util.List;

public abstract class TagService extends EntityService<Tag> {

    public abstract Tag getByName(String name) throws EntityNotFoundException;

    public abstract List<Tag> getAll();
}
