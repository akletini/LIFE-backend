package akletini.life.core.product.dto.mapper;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public abstract class AttributeTypeMapper {
    @Autowired
    protected ProductTypeService productTypeService;
    @Autowired
    @Lazy
    protected ProductTypeMapper productTypeMapper;

    @Mapping(target = "inheritedBy", expression = "java(attributeTypeDto.getInheritedBy() != null" +
            " ? attributeTypeDto.getInheritedBy().getId() : null)")
    public abstract AttributeType dtoToAttributeType(AttributeTypeDto attributeTypeDto);

    @Mapping(target = "inheritedBy", expression = "java(attributeType.getInheritedBy() != null ? " +
            "productTypeMapper.productTypeToDto(productTypeService.getById(attributeType" +
            ".getInheritedBy())" +
            ") : null)")
    public abstract AttributeTypeDto attributeTypeToDto(AttributeType attributeType) throws EntityNotFoundException;
}
