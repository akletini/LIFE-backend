package akletini.life.core.product.service;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;

public abstract class AttributeTypeService extends EntityService<AttributeType> {
    public abstract AttributeType getByName(String typeName) throws EntityNotFoundException;
}
