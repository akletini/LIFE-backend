package akletini.life.core.user.dto;

import akletini.life.core.user.repository.entity.AuthProvider;
import akletini.life.core.user.repository.entity.TokenContainer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String imageUrl;
    private boolean loggedIn;
    private String jwtToken;
    private AuthProvider authProvider;
    private TokenContainer tokenContainer;
}
