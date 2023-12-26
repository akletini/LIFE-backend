package akletini.life.core.product.repository.entity;

import akletini.life.core.shared.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "producttypes")
@Document(indexName = "product_types")
public class ProductType extends NamedEntity {

    @Field(type = FieldType.Long)
    private Long parentProductType;

    @Transient
    @ToString.Exclude
    private List<ProductType> childProductTypes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_types_id")
    @ToString.Exclude
    @Field(type = FieldType.Nested)
    private List<AttributeType> attributeTypes;
}
