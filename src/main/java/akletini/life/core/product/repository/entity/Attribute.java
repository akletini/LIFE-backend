package akletini.life.core.product.repository.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
    private AttributeType attributeType;
    private String value;
}
