package akletini.life.core.product.dto.mapper;

import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.repository.entity.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface ProductMapper {
    Product dtoToProduct(ProductDto productDto);

    ProductDto productToDto(Product product);
}
