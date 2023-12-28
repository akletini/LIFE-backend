package akletini.life.vaadin.service.product;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.dto.mapper.AttributeTypeMapper;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExposedAttributeTypeService {
    private final AttributeTypeMapper attributeTypeMapper;
    private final AttributeTypeService attributeTypeService;

    public List<AttributeTypeDto> getAll() throws EntityNotFoundException {
        List<AttributeTypeDto> list = new ArrayList<>();
        for (AttributeType attributeType : attributeTypeService.getAll()) {
            AttributeTypeDto attributeTypeToDto =
                    attributeTypeMapper.attributeTypeToDto(attributeType);
            list.add(attributeTypeToDto);
        }
        return list;
    }

    public Page<AttributeTypeDto> getAttributeTypes(int page, int pageSize, String searchTerm) throws IOException, EntityNotFoundException {
        Page<AttributeType> attributeTypes = attributeTypeService.getAttributeTypes(page,
                pageSize, searchTerm);
        List<AttributeTypeDto> list = new ArrayList<>();
        for (AttributeType attributeType : attributeTypes) {
            AttributeTypeDto attributeTypeToDto =
                    attributeTypeMapper.attributeTypeToDto(attributeType);
            list.add(attributeTypeToDto);
        }
        return new PageImpl<>(list, attributeTypes.getPageable(),
                attributeTypes.getTotalElements());
    }

    public AttributeTypeDto store(AttributeTypeDto attributeTypeDto) throws BusinessException {
        return attributeTypeMapper.attributeTypeToDto(attributeTypeService.store(attributeTypeMapper.dtoToAttributeType(attributeTypeDto)));
    }
}
