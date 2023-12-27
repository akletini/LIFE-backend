package akletini.life.core.shared;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

import static akletini.life.core.shared.utils.DateUtils.LOCAL_DATE_TIME_FORMAT;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NonNull
    @Field(type = FieldType.Date, format = {}, pattern = LOCAL_DATE_TIME_FORMAT)
    protected final LocalDateTime createdAt = LocalDateTime.now();

    @Field
    @ReadOnlyProperty
    @AccessType(AccessType.Type.PROPERTY)
    public void set_class(String _class) {
    }
}
