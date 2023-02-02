package akletini.life.todo.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "tag")
    @ToString.Exclude
    private List<Todo> assignedTodos;

    @PreRemove
    private void preRemove() {
        assignedTodos.forEach(todo -> todo.setTag(null));
    }
}
