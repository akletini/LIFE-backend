package akletini.life.user.dto;

import akletini.life.user.repository.entity.AuthProvider;
import akletini.life.user.repository.entity.TokenContainer;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean loggedIn;
    private AuthProvider authProvider;
    private TokenContainer tokenContainer;
}
