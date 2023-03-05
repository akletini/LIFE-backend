package akletini.life.chore.validation.rules;

import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static akletini.life.shared.utils.DateUtils.localDateTimeToDate;
import static akletini.life.shared.validation.Errors.TODO.CREATED_DATE_UNCHANGED;

@Component("choreCreatedDateUnchangedRule")
public class CreatedDateUnchangedRule implements ValidationRule<Chore> {

    @Autowired
    private ChoreRepository choreRepository;

    @Override
    public Optional<String> validate(Chore chore) {
        if (chore.getId() != null) {
            Optional<Chore> byId = choreRepository.findById(chore.getId());
            if (byId.isPresent()) {
                Chore loadedTodo = byId.get();
                if (!DateUtils.isSameInstant(localDateTimeToDate(loadedTodo.getCreatedAt()),
                        localDateTimeToDate(chore.getCreatedAt()))) {
                    return Optional.of(Errors.getError(CREATED_DATE_UNCHANGED));
                }
            }
        }
        return Optional.empty();
    }
}
