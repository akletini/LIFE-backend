package akletini.life.core.product.service;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public abstract class AttributeTypeService extends EntityService<AttributeType> {
    public abstract AttributeType getByName(String typeName) throws EntityNotFoundException;

    public abstract List<ProductType> getProductTypesByAttributeType(AttributeType attributeType);

    public abstract Page<AttributeType> getAttributeTypes(int page,
                                                          int pageSize,
                                                          String searchTerm) throws IOException;
}
