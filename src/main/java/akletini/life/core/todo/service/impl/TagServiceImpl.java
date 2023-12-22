package akletini.life.core.todo.service.impl;

import akletini.life.core.todo.exception.TagNotFoundException;
import akletini.life.core.todo.exception.TagStoreException;
import akletini.life.core.todo.repository.api.TagRepository;
import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.service.api.TagService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag store(Tag tag) {
        if (tag != null) {
            validateUniqueName(tag);
            return tagRepository.save(tag);
        }
        throw new TagStoreException("Could not store Tag object");
    }

    @Override
    public Tag getById(Long id) {
        Optional<Tag> tagById = tagRepository.findById(id);
        if (tagById.isPresent()) {
            return tagById.get();
        }
        throw new TagNotFoundException("Could not find Tag with ID " + id);
    }

    @Override
    public Tag getByName(String name) {
        Optional<Tag> tagByName = tagRepository.findByName(name);
        return tagByName.orElseThrow(() -> new TagNotFoundException("Could not find Tag with name" +
                " " + name));
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

    private void validateUniqueName(Tag tag) {
        Optional<Tag> getByName = tagRepository.findByName(tag.getName());
        if (getByName.isPresent()) {
            if (!Objects.equals(getByName.get().getId(), tag.getId())) {
                throw new TagStoreException("Tag name is not unique");
            }
        }
    }
}
