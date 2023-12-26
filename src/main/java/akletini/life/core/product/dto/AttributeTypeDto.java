package akletini.life.core.product.dto;

import akletini.life.core.product.repository.entity.BasicType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttributeTypeDto {
    private String name;

    private boolean required;

    private BasicType basicType;
}
