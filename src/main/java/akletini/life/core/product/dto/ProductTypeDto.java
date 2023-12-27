package akletini.life.core.product.dto;

import akletini.life.core.shared.dto.NamedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductTypeDto extends NamedDto {

    private Long parentProductType;

    private List<ProductTypeDto> childProductTypes = new ArrayList<>();

    private List<AttributeTypeDto> attributeTypes;
}
