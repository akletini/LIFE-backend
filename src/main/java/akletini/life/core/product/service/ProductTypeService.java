package akletini.life.core.product.service;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;

import java.util.List;

public abstract class ProductTypeService extends EntityService<ProductType> {

    public abstract ProductType getByName(String name) throws EntityNotFoundException;

    public abstract List<AttributeType> getAttributeTypesForProductType(ProductType productType) throws EntityNotFoundException;
}
