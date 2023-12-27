package akletini.life.core.product.repository.entity;

import akletini.life.core.shared.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
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
}
