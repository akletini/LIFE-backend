package akletini.life.core.user.service.impl;

import akletini.life.core.user.repository.api.UserRepository;
import akletini.life.core.user.repository.entity.User;
import akletini.life.core.user.service.api.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl extends UserService {

    private UserRepository userRepository;


    @Override
    public User getByEmail(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }
        throw new RuntimeException("User not found");
    }

}
