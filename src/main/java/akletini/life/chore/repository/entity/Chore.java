package akletini.life.chore.repository.entity;

import akletini.life.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "chores")
public class Chore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;
    private String createdAt;
    private String dueAt;

    private String startDate;
    private String description;
    private boolean isActive;

    // duration in minutes;
    private int duration;

    private Interval interval;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;
}
