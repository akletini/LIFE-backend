package akletini.life.core.user.service.api;

import akletini.life.core.shared.EntityService;
import akletini.life.core.user.repository.entity.User;

import java.util.List;

public interface UserService extends EntityService<User> {

    User getByEmail(String email);

    List<User> getAll();
}
