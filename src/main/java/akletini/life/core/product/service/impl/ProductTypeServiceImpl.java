package akletini.life.core.product.service.impl;

import akletini.life.core.product.repository.api.productType.ProductTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ProductTypeServiceImpl extends ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    @Override
    public ProductType getByName(String typeName) throws EntityNotFoundException {
        return productTypeRepository.findByName(typeName).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            AttributeType.class.getSimpleName(), typeName));
            log.error(exception);
            return exception;
        });
    }

    @Override
    public List<AttributeType> getAttributeTypesForProductType(ProductType productType) throws EntityNotFoundException {
        if (productType != null) {
            Optional<ProductType> productTypeOptional =
                    entityRepository.findById(productType.getId());
            ProductType foundPropertyType = productTypeOptional.orElseThrow(() -> {
                EntityNotFoundException entityNotFoundException =
                        new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                                ProductType.class.getSimpleName(), productType.getId()));
                log.error(entityNotFoundException);
                return entityNotFoundException;
            });
            return foundPropertyType.getAttributeTypes();
        }
        return new ArrayList<>();
    }


}
