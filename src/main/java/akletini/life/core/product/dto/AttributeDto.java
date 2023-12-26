package akletini.life.core.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttributeDto {
    private AttributeTypeDto attributeType;
    private String value;
}
