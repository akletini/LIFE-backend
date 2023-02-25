package akletini.life.user.repository.entity;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.todo.repository.entity.Todo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    private String imageUrl;
    private boolean loggedIn;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    private TokenContainer tokenContainer;

    @OneToMany(mappedBy = "assignedUser")
    @ToString.Exclude
    private List<Todo> assignedTodos;

    @OneToMany(mappedBy = "assignedUser")
    @ToString.Exclude
    private List<Chore> assignedChores;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
