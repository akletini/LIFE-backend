package akletini.life.core.product.repository.entity;

import akletini.life.core.shared.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "attributetypes")
@Document(indexName = "attribute_types")
public class AttributeType extends NamedEntity implements Serializable {

    @Field(index = false)
    private boolean required;

    @Field(index = false)
    @Enumerated(EnumType.STRING)
    private BasicType basicType;

    /**
     * ID of the product type from which this value is inherited. Null, if it is not inherited.
     */
    @Transient
    private Long inheritedBy;
}
