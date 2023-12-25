package akletini.life.core.product.validation;

import akletini.life.core.product.repository.entity.Product;
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
public class ProductValidation implements EntityValidation<Product> {

    private final EntityNotNullRule<Product> entityNotNullRule;
    private final NamedEntityAlreadyExistsRule<Product> entityAlreadyExistsRule;

    @Override
    public List<ValidationRule<Product>> getValidationRules() {
        ArrayList<ValidationRule<Product>> rules = new ArrayList<>();
        rules.add(entityNotNullRule);
        rules.add(entityAlreadyExistsRule);
        return rules;
    }
}
