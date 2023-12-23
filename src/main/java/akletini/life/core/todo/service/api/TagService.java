package akletini.life.core.todo.service.api;

import akletini.life.core.shared.EntityService;
import akletini.life.core.todo.repository.entity.Tag;

import java.util.List;

public interface TagService extends EntityService<Tag> {

    Tag getByName(String name);

    List<Tag> getAll();
}
