package akletini.life.core.product.repository.entity;

import akletini.life.core.product.repository.entity.converter.AttributeListConverter;
import akletini.life.core.shared.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "products")
@Document(indexName = "products")
public class Product extends NamedEntity {

    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Nested)
    private Quantity quantity;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;
    @Field(type = FieldType.Nested, includeInParent = true)
    @Convert(converter = AttributeListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Attribute> attributes;

}
