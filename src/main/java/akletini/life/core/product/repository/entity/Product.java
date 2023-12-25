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

import java.time.LocalDateTime;
import java.util.List;

import static akletini.life.core.shared.utils.DateUtils.LOCAL_DATE_TIME_FORMAT;

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
    @Field(type = FieldType.Date, format = {}, pattern = LOCAL_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;
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
