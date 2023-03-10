package akletini.life.user.repository.entity.auth;

import akletini.life.user.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponse {
    private String token;

    private User user;
}
