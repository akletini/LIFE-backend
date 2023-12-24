package akletini.life.core.product.validation;

import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.shared.validation.EntityValidation;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.rule.EntityNotNullRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductValidation implements EntityValidation<Product> {

    private EntityNotNullRule<Product> entityNotNullRule;

    @Override
    public List<ValidationRule<Product>> getValidationRules() {
        return List.of(entityNotNullRule);
    }
}
