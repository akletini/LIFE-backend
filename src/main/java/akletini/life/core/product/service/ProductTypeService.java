package akletini.life.core.product.service;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;

import java.util.List;
import java.util.Map;

public abstract class ProductTypeService extends EntityService<ProductType> {

    public abstract ProductType getByName(String name) throws EntityNotFoundException;

    public abstract List<AttributeType> getAttributeTypesForProductType(ProductType productType) throws EntityNotFoundException;

    public abstract Map<ProductType, List<ProductType>> constructHierarchy();

    public abstract List<ProductType> getProductTypesUpToRoot(ProductType startProductType);

    public abstract List<ProductType> getAllChildProductTypes(ProductType parentProductType);
}
