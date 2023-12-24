package akletini.life.core.todo.repository.entity;

import akletini.life.core.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tags")
public class Tag extends BaseEntity implements Serializable {

    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "tag")
    @ToString.Exclude
    private List<Todo> assignedTodos;

    @PreRemove
    private void preRemove() {
        if (assignedTodos != null) {
            assignedTodos.forEach(todo -> todo.setTag(null));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
