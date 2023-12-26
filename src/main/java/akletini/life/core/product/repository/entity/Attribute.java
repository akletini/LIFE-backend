package akletini.life.core.product.repository.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
    private AttributeType attributeType;
    @Field(type = FieldType.Keyword, includeInParent = true)
    private String value;
}
