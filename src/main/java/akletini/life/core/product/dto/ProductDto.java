package akletini.life.core.product.dto;

import akletini.life.core.product.repository.entity.Quantity;
import akletini.life.core.shared.dto.NamedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDto extends NamedDto {
    private String description;
    private LocalDateTime createdAt;
    private Quantity quantity = new Quantity();
    private ProductTypeDto productType;
    private List<AttributeDto> attributes = new ArrayList<>();
}
