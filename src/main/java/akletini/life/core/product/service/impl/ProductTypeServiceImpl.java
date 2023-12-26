package akletini.life.core.product.service.impl;

import akletini.life.core.product.repository.api.productType.ProductTypeIndexRepository;
import akletini.life.core.product.repository.api.productType.ProductTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ProductTypeServiceImpl extends ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeIndexRepository productTypeIndexRepository;

    @Override
    public ProductType store(ProductType productType) throws BusinessException {
        ProductType stored = super.store(productType);
        addInheritedAttributeTypes(stored);
        productTypeIndexRepository.save(stored);
        return stored;
    }

    @Override
    public ProductType getById(Long id) throws EntityNotFoundException {
        ProductType productType = super.getById(id);
        addInheritedAttributeTypes(productType);
        return productType;
    }

    @Override
    public List<ProductType> getAll() {
        List<ProductType> all = super.getAll();
        all.forEach(this::addInheritedAttributeTypes);
        return all;
    }

    @Override
    public ProductType getByName(String typeName) throws EntityNotFoundException {
        ProductType productType = productTypeRepository.findByName(typeName).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            AttributeType.class.getSimpleName(), typeName));
            log.error(exception);
            return exception;
        });
        addInheritedAttributeTypes(productType);
        return productType;
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
            addInheritedAttributeTypes(foundPropertyType);
            return foundPropertyType.getAttributeTypes();
        }
        return new ArrayList<>();
    }

    private void addInheritedAttributeTypes(ProductType productType) {
        List<AttributeType> attributeTypes = productType.getAttributeTypes() != null ?
                new ArrayList<>(productType.getAttributeTypes()) : new ArrayList<>();
        Long parentId = productType.getParentProductType();
        while (parentId != null) {
            ProductType parentProductType =
                    productTypeRepository.findById(parentId).orElseThrow();
            attributeTypes.addAll(parentProductType.getAttributeTypes());
            parentId = parentProductType.getParentProductType();
        }
        productType.setAttributeTypes(new LinkedHashSet<>(attributeTypes).stream().toList());
    }


}
