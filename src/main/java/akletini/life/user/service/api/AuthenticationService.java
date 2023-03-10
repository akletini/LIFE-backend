package akletini.life.user.service.api;

import akletini.life.user.repository.entity.User;
import akletini.life.user.repository.entity.auth.AuthenticationRequest;
import akletini.life.user.repository.entity.auth.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(User user);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
