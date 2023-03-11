package akletini.life.task.entity;

import akletini.life.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NonNull
    protected String title;

    @NonNull
    protected LocalDateTime createdAt;
    @NonNull
    protected LocalDate dueAt;
    protected String description;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    protected User assignedUser;

    @PreRemove
    protected void preRemove() {
        assignedUser = null;
    }
}
