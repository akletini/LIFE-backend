package akletini.life.core.product.dto.mapper;

import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.core.product.repository.entity.ProductType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = AttributeTypeMapper.class)
public interface ProductTypeMapper {

    ProductType dtoToProductType(ProductTypeDto productTypeDto);

    ProductTypeDto productTypeToDto(ProductType productType);
}
