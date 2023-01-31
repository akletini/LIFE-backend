package akletini.life.todo.service.api;

import akletini.life.todo.repository.entity.Tag;

import java.util.List;
public interface TagService {

    Tag store(Tag tag);

    Tag getById(Long id);

    List<Tag> getAll();

    void delete(Long id);
}
