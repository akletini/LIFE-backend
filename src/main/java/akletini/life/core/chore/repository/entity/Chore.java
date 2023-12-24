package akletini.life.core.chore.repository.entity;

import akletini.life.core.task.entity.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "chores")
public class Chore extends Task {

    @NonNull
    private LocalDate startDate;

    private LocalDate lastCompleted;

    private boolean active;
    private boolean shiftInterval;

    // duration in minutes;
    private Integer duration;

    private Interval interval;

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
