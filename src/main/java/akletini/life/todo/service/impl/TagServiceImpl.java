package akletini.life.todo.service.impl;

import akletini.life.todo.exception.custom.TodoNotFoundException;
import akletini.life.todo.repository.api.TagRepository;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.service.api.TagService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag store(Tag tag) {
        if (tag != null) {
            return tagRepository.save(tag);
        }
        throw new RuntimeException("Could not store Tag object");
    }

    @Override
    public Tag getById(Long id) {
        Optional<Tag> tagById = tagRepository.findById(id);
        if (tagById.isPresent()) {
            return tagById.get();
        }
        throw new TodoNotFoundException("Could not find Tag with ID " + id);
    }

    @Override
    public List<Tag> getAll() {
        Iterable<Tag> allTags = tagRepository.findAll();
        return IterableUtils.toList(allTags);
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

}
