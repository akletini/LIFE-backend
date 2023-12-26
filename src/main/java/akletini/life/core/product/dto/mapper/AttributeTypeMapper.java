package akletini.life.core.product.dto.mapper;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.repository.entity.AttributeType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface AttributeTypeMapper {
    AttributeType dtoToAttributeType(AttributeTypeDto attributeTypeDto);

    AttributeTypeDto attributeTypeToDto(AttributeType attributeType);
}
