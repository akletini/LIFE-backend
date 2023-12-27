package akletini.life.vaadin.service.product;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.dto.mapper.AttributeTypeMapper;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.shared.validation.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExposedAttributeTypeService {
    private final AttributeTypeMapper attributeTypeMapper;
    private final AttributeTypeService attributeTypeService;

    public List<AttributeTypeDto> getAll() {
        return attributeTypeService.getAll().stream().map(attributeTypeMapper::attributeTypeToDto).collect(Collectors.toList());
    }

    public Page<AttributeTypeDto> getAttributeTypes(int page, int pageSize, String searchTerm) throws IOException {
        Page<AttributeType> attributeTypes = attributeTypeService.getAttributeTypes(page,
                pageSize, searchTerm);
        return attributeTypes.map(attributeTypeMapper::attributeTypeToDto);
    }

    public AttributeTypeDto store(AttributeTypeDto attributeTypeDto) throws BusinessException {
        return attributeTypeMapper.attributeTypeToDto(attributeTypeService.store(attributeTypeMapper.dtoToAttributeType(attributeTypeDto)));
    }
}
