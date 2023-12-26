package akletini.life.core.product.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductTypeDto {

    private Long id;
    private String name;
    private Long parentProductType;

    private List<ProductTypeDto> childProductTypes = new ArrayList<>();

    private List<AttributeTypeDto> attributeTypes;
}
