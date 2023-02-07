package akletini.life.user.service;

import akletini.life.user.repository.entity.User;

public interface UserService {

    User store(User user);

    void deleteUser(User user);

    User getById(Long id);

    User getByEmail(String email);
}
