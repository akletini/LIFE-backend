package akletini.life.core.user.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}