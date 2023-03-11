package akletini.life.todo.repository.entity;

import akletini.life.task.entity.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.File;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "todos")
public class Todo extends Task {
    public enum State {OPEN, DONE}

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "TAG_ID", referencedColumnName = "ID")
    private Tag tag;

    private File attachedFile;
    @Enumerated(EnumType.STRING)
    private State state;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Todo todo = (Todo) o;
        return id != null && Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
