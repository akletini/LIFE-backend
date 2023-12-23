package akletini.life.core.todo.service.impl;

import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.EntityNullException;
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
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    private EntityValidation<Tag> tagValidation;

    @Override
    public boolean validate(Tag tag) {
        List<ValidationRule<Tag>> validationRules = tagValidation.getValidationRules();
        validationRules.forEach(rule -> {
            Optional<String> error = rule.validate(tag);
            if (error.isPresent()) {
                InvalidDataException invalidDataException = new InvalidDataException(error.get());
                log.error(invalidDataException);
                throw invalidDataException;
            }
        });
        return true;
    }

    public Tag store(Tag tag) {
        validate(tag);
        return tagRepository.save(tag);
    }

    @Override
    public Tag getById(Long id) {
        Optional<Tag> tagById = tagRepository.findById(id);
        if (tagById.isPresent()) {
            return tagById.get();
        }
        throw new EntityNotFoundException("Could not find Tag with ID " + id);
    }

    @Override
    public void delete(Tag tag) {
        if (tag != null) {
            tagRepository.deleteById(tag.getId());
        }

    }

    @Override
    public Tag getByName(String name) {
        Optional<Tag> tagByName = tagRepository.findByName(name);
        return tagByName.orElseThrow(() -> new EntityNullException("Could not find Tag with name" +
                " " + name));
    }

    @Override
    public List<Tag> getAll() {
        Iterable<Tag> allTags = tagRepository.findAll();
        return IterableUtils.toList(allTags);
    }
}
