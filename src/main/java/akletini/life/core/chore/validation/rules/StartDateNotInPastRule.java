package akletini.life.core.chore.validation.rules;

import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.utils.DateUtils;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class StartDateNotInPastRule implements ValidationRule<Chore> {

    @Override
    public Optional<String> validate(Chore chore) {
        if (chore.getId() != null) {
            return Optional.empty();
        }
        Date startDate = DateUtils.localDateToDate(chore.getStartDate());
        if (DateUtils.truncatedCompareTo(startDate, new Date(), Calendar.DATE) < 0) {
            return Optional.of(Errors.getError(Errors.CHORE.START_IN_THE_PAST));
        }
        return Optional.empty();
    }
}
