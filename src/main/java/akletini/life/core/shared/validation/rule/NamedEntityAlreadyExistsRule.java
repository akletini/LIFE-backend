package akletini.life.core.shared.validation.rule;

import akletini.life.core.product.repository.api.attributeType.AttributeTypeRepository;
import akletini.life.core.product.repository.api.product.ProductRepository;
import akletini.life.core.product.repository.api.productType.ProductTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.NamedEntity;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityAlreadyExistsException;
import akletini.life.core.todo.repository.api.TagRepository;
import akletini.life.core.todo.repository.entity.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class NamedEntityAlreadyExistsRule<T extends NamedEntity> implements ValidationRule<T> {

    private AttributeTypeRepository attributeTypeRepository;
    private ProductRepository productRepository;
    private ProductTypeRepository productTypeRepository;
    private TagRepository tagRepository;

    @Override
    public Optional<BusinessException> validate(T validatable) {
        Optional<?> getByName = Optional.empty();
        if (validatable instanceof AttributeType) {
            getByName = attributeTypeRepository.findByName(validatable.getName());
        } else if (validatable instanceof Product) {
            getByName = productRepository.findByName(validatable.getName());
        } else if (validatable instanceof ProductType) {
            getByName = productTypeRepository.findByName(validatable.getName());
        } else if (validatable instanceof Tag) {
            getByName = tagRepository.findByName(validatable.getName());
        }

        if (getByName.isPresent()) {
            NamedEntity entity = (NamedEntity) getByName.get();
            if (!entity.getId().equals(validatable.getId())) {
                return Optional.of(new EntityAlreadyExistsException(Errors.getError(Errors.ENITTY_ALREADY_EXISTS, validatable.getName())));
            }
        }
        return Optional.empty();
    }
}
