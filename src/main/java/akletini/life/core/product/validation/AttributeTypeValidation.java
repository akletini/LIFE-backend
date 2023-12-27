package akletini.life.core.product.validation;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.validation.rule.BasicTypeMigrationRule;
import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.rule.EntityNotNullRule;
import akletini.life.core.shared.validation.rule.NamedEntityAlreadyExistsRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AttributeTypeValidation implements EntityValidation<AttributeType> {
    private final NamedEntityAlreadyExistsRule<AttributeType> entityAlreadyExistsRule;
    private final EntityNotNullRule<AttributeType> entityNotNullRule;
    private final BasicTypeMigrationRule basicTypeMigrationRule;

    @Override
    public List<ValidationRule<AttributeType>> getValidationRules() {
        ArrayList<ValidationRule<AttributeType>> rules = new ArrayList<>();
        rules.add(entityAlreadyExistsRule);
        rules.add(entityNotNullRule);
        rules.add(basicTypeMigrationRule);
        return rules;
    }
}
