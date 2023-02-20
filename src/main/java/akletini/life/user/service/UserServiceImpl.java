package akletini.life.user.service;

import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.AuthProvider;
import akletini.life.user.repository.entity.TokenContainer;
import akletini.life.user.repository.entity.User;
import org.apache.commons.lang3.StringUtils;
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
    public boolean validate(User user) {
        return false;
    }

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
    public User getById(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get();
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getByEmail(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }
        throw new RuntimeException("User not found");
    }

    private void validateProviders(User user) {
        if (user.getEmail() == null) {
            throw new RuntimeException("Missing email, cannot store user");
        }
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
        } else if (AuthProvider.CREDENTIALS.equals(user.getAuthProvider())) {
            if (StringUtils.isEmpty(user.getPassword())) {
                throw new RuntimeException("Missing password, cannot store user");
            }
        }
    }
}
