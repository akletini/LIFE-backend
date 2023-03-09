package akletini.life.chore.repository.entity;

import akletini.life.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "chores")
public class Chore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NonNull
    private String title;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDate dueAt;
    @NonNull
    private LocalDate startDate;

    private LocalDate lastCompleted;
    private String description;
    private boolean active;
    private boolean shiftInterval;

    // duration in minutes;
    @NonNull
    private Integer duration;

    @NonNull
    private Interval interval;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

    @PreRemove
    private void preRemove() {
        assignedUser = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Chore chore = (Chore) o;
        return id != null && Objects.equals(id, chore.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
