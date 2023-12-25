package akletini.life.core.task.entity;

import akletini.life.core.shared.BaseEntity;
import akletini.life.core.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Task extends BaseEntity {

    @NonNull
    protected String title;

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
