package akletini.life.core.product.dto;

import akletini.life.core.product.repository.entity.Attribute;
import akletini.life.core.product.repository.entity.Quantity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Quantity quantity;
    private List<Attribute> attributes;
}
