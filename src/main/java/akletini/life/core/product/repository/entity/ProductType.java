package akletini.life.core.product.repository.entity;

import akletini.life.core.shared.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "producttypes")
public class ProductType extends NamedEntity {

    private Long parentProductType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_types_id")
    @ToString.Exclude
    private List<AttributeType> attributeTypes;
}
