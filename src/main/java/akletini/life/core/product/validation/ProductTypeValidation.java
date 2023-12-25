package akletini.life.core.product.validation;

import akletini.life.core.product.repository.entity.ProductType;
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
public class ProductTypeValidation implements EntityValidation<ProductType> {
    private final NamedEntityAlreadyExistsRule<ProductType> entityAlreadyExistsRule;
    private final EntityNotNullRule<ProductType> entityNotNullRule;

    @Override
    public List<ValidationRule<ProductType>> getValidationRules() {
        ArrayList<ValidationRule<ProductType>> rules = new ArrayList<>();
        rules.add(entityNotNullRule);
        rules.add(entityAlreadyExistsRule);
        return rules;
    }
}
