package akletini.life.core.todo.service.impl;

import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import akletini.life.core.todo.repository.api.TagRepository;
import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.service.api.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class TagServiceImpl extends TagService {

    private TagRepository tagRepository;

    public Tag store(Tag tag) throws InvalidDataException {
        validate(tag);
        return tagRepository.save(tag);
    }

    @Override
    public void delete(Tag tag) {
        if (tag != null) {
            tagRepository.deleteById(tag.getId());
        }
    }

    @Override
    public Tag getByName(String name) throws EntityNotFoundException {
        Optional<Tag> tagByName = tagRepository.findByName(name);
        return tagByName.orElseThrow(() -> new EntityNotFoundException("Could not find Tag with " +
                "name " + name));
    }

    @Override
    public List<Tag> getAll() {
        Iterable<Tag> allTags = tagRepository.findAll();
        return IterableUtils.toList(allTags);
    }
}
