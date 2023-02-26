package akletini.life.chore;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static akletini.life.shared.utils.DateUtils.DATE_TIME_FORMAT;

public class TestChores {

    public static Chore getNewChore() {
        Chore chore = new Chore();
        chore.setTitle("Title");
        chore.setActive(true);
        try {
            chore.setCreatedAt(new SimpleDateFormat(DATE_TIME_FORMAT).parse("01.02.2023 14:00:00"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        chore.setStartDate(new Date());
        chore.setShiftInterval(false);
        chore.setDuration(60);
        chore.setInterval(new Interval(5, Interval.DateUnit.DAYS));
        return chore;
    }
}
