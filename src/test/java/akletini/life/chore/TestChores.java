package akletini.life.chore;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static akletini.life.shared.utils.DateUtils.DATE_TIME_FORMAT;

public class TestChores {

    public static Chore getNewChore() {
        Chore chore = new Chore();
        chore.setTitle("Title");
        chore.setActive(true);
        chore.setCreatedAt(LocalDateTime.parse("01.02.2023 14:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        chore.setStartDate(LocalDate.now());
        chore.setShiftInterval(false);
        chore.setDuration(60);
        chore.setInterval(new Interval(5, Interval.DateUnit.DAYS));
        return chore;
    }
}
