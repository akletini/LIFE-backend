package akletini.life.core.product.dto.mapper;

import akletini.life.core.product.dto.AttributeDto;
import akletini.life.core.product.repository.entity.Attribute;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = AttributeTypeMapper.class)
public interface AttributeMapper {
    Attribute dtoToAttribute(AttributeDto attributeDto);

    AttributeDto attributeToDto(Attribute attributeType);
}
