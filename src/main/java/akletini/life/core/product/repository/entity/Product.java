package akletini.life.core.product.repository.entity;

import akletini.life.core.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @NotNull
    private String name;
    private String description;
    @NotNull
    private LocalDateTime createdAt;
    private Quantity quantity;
    @Transient
    private List<Attribute> attributes;
}
