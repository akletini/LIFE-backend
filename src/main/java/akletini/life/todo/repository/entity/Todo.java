package akletini.life.todo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todos")
public class Todo {
    private enum State {OPEN, DONE }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String createdAt;
    private String dueAt;
    private String description;

    @ManyToOne
    @JoinColumn(name = "TAG_ID", referencedColumnName = "ID")
    private Tag tag;

    private File attachedFile;
    @Enumerated(EnumType.STRING)
    private State state;
}
