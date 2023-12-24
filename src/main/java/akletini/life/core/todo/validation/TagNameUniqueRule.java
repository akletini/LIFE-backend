package akletini.life.core.todo.validation;

import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.todo.repository.api.TagRepository;
import akletini.life.core.todo.repository.entity.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TagNameUniqueRule implements ValidationRule<Tag> {

    private TagRepository tagRepository;

    @Override
    public Optional<String> validate(Tag tag) {
        Optional<Tag> getByName = tagRepository.findByName(tag.getName());
        if (getByName.isPresent()) {
            if (!Objects.equals(getByName.get().getId(), tag.getId())) {
                return Optional.of(Errors.getError(Errors.TAG.TAG_NAME_UNIQUE));
            }
        }
        return Optional.empty();
    }
}
