package akletini.life.user.service.api;

import akletini.life.shared.EntityService;
import akletini.life.user.repository.entity.User;

import java.util.List;

public interface UserService extends EntityService<User> {

    User getByEmail(String email);

    List<User> getAll();
}
