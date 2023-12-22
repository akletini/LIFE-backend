package akletini.life.core.todo.service.api;

import akletini.life.core.todo.repository.entity.Tag;

import java.util.List;

public interface TagService {

    Tag store(Tag tag);

    Tag getById(Long id);

    Tag getByName(String name);

    List<Tag> getAll();

    void delete(Long id);
}
