package akletini.life.user.service;

import akletini.life.shared.EntityService;
import akletini.life.user.repository.entity.User;

public interface UserService extends EntityService<User> {

    User getByEmail(String email);
}
