package akletini.life.core.user.service.api;

import akletini.life.core.shared.EntityService;
import akletini.life.core.user.repository.entity.User;

public abstract class UserService extends EntityService<User> {

    public abstract User getByEmail(String email);
}
