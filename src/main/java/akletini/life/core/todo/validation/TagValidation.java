package akletini.life.core.todo.validation;

import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.rule.EntityNotNullRule;
import akletini.life.core.todo.repository.entity.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TagValidation implements EntityValidation<Tag> {

    private EntityNotNullRule<Tag> entityNotNullRule;
    private TagNameUniqueRule tagNameUniqueRule;

    @Override
    public List<ValidationRule<Tag>> getValidationRules() {
        List<ValidationRule<Tag>> rules = new ArrayList<>();
        rules.add(entityNotNullRule);
        rules.add(tagNameUniqueRule);
        return rules;
    }
}
