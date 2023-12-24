package akletini.life.core.user.repository.entity;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.BaseEntity;
import akletini.life.core.todo.repository.entity.Todo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String name;
    @Column(unique = true)
    @NonNull
    private String email;
    private String password;

    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @NonNull
    private AuthProvider authProvider;
    private TokenContainer tokenContainer;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Token> tokens;

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
