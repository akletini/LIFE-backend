package akletini.life.core.product.validation.rule.productType;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class DuplicateAttributesRule implements ValidationRule<ProductType> {
    @Override
    public Optional<BusinessException> validate(ProductType validatable) {
        List<AttributeType> attributeTypes = validatable.getAttributeTypes();
        HashSet<AttributeType> uniqueAttributeTypes = new HashSet<>(attributeTypes);
        if (attributeTypes.size() > uniqueAttributeTypes.size()) {
            return Optional.of(new InvalidDataException(Errors.getError(Errors.PRODUCT_TYPE.DUPLICATE_ATTRIBUTES)));
        }
        return Optional.empty();
    }
}
