package akletini.life.chore.validation.rules;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.shared.validation.ValidationRule;

import java.util.Optional;

public class PositiveIntervalRule implements ValidationRule<Chore> {
    @Override
    public Optional<String> validate(Chore chore) {
        if (chore.getInterval().getValue() <= 0) {
            return Optional.of("Only positive intervals are allowed");
        }
        return Optional.empty();
    }
}
