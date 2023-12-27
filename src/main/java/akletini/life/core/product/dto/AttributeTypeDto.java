package akletini.life.core.product.dto;

import akletini.life.core.product.repository.entity.BasicType;
import akletini.life.core.shared.dto.NamedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AttributeTypeDto extends NamedDto {
    private boolean required;

    private BasicType basicType;
}
