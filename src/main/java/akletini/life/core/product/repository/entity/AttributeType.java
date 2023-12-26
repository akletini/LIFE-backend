package akletini.life.core.product.repository.entity;

import akletini.life.core.shared.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "attributetypes")
@Document(indexName = "attribute_types")
public class AttributeType extends NamedEntity implements Serializable {

    @Column(unique = true)
    private String name;

    private boolean required;

    @Enumerated(EnumType.STRING)
    private BasicType basicType;
}
