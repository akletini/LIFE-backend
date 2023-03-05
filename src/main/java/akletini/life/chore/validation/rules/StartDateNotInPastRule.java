package akletini.life.chore.validation.rules;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.shared.utils.DateUtils;
import akletini.life.shared.validation.Errors;
import akletini.life.shared.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static akletini.life.shared.utils.DateUtils.localDateToDate;

@Component
public class StartDateNotInPastRule implements ValidationRule<Chore> {

    @Override
    public Optional<String> validate(Chore chore) {
        Date startDate = localDateToDate(chore.getStartDate());
        if (DateUtils.truncatedCompareTo(startDate, new Date(), Calendar.DATE) < 0) {
            return Optional.of(Errors.getError(Errors.CHORE.START_IN_THE_PAST));
        }
        return Optional.empty();
    }
}
