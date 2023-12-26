package akletini.life.core.product.service.impl;

import akletini.life.core.product.repository.api.attributeType.AttributeTypeIndexRepository;
import akletini.life.core.product.repository.api.attributeType.AttributeTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class AttributeTypeServiceImpl extends AttributeTypeService {

    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeTypeIndexRepository attributeTypeIndexRepository;

    @Override
    public AttributeType store(AttributeType attributeType) throws BusinessException {
        AttributeType stored = super.store(attributeType);
        attributeTypeIndexRepository.save(attributeType);
        return stored;
    }

    @Override
    public AttributeType getByName(String typeName) throws EntityNotFoundException {
        return attributeTypeRepository.findByName(typeName).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            AttributeType.class.getSimpleName(), typeName));
            log.error(exception);
            return exception;
        });
    }
}
