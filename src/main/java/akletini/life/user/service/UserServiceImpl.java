package akletini.life.user.service;

import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.AuthProvider;
import akletini.life.user.repository.entity.TokenContainer;
import akletini.life.user.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User store(User user) {
        if (user != null) {
            if (user.getId() == null) {
                validateProviders(user);
                user.setLoggedIn(true);
            }
            return userRepository.save(user);
        }
        throw new RuntimeException(String.format("Could not store %s object", User.class.getName()));
    }

    @Override
    public void loginUser(User user) {

    }

    @Override
    public void logoutUser(User user) {

    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getById(Long id) {
        Optional<User> byEmail = userRepository.findById(id);
        if (byEmail.isPresent()) {
            return byEmail.get();
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public User getByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return byEmail.get();
        }
        throw new RuntimeException("User not found");
    }

    private void validateProviders(User user) {
        if (AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
            TokenContainer tokenContainer = user.getTokenContainer();
            boolean allNonNull = false;
            if (tokenContainer != null) {
                allNonNull = Stream.of(tokenContainer.getAccessToken(),
                                tokenContainer.getRefreshToken(),
                                tokenContainer.getAccessTokenCreation())
                        .allMatch(Objects::nonNull);
            }
            if (!allNonNull) {
                throw new RuntimeException("Not all fields are filled out for" + TokenContainer.class.getName());
            }
        }
    }
}
