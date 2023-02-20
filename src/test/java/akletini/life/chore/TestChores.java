package akletini.life.chore;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;
import akletini.life.shared.utils.DateUtils;

import java.util.Date;

public class TestChores {

    public static Chore getNewChore() {
        Chore chore = new Chore();
        chore.setTitle("Title");
        chore.setActive(true);
        chore.setCreatedAt("01.02.2023 14:00:00");
        chore.setStartDate(DateUtils.dateToString(new Date()));
        chore.setShiftInterval(false);
        chore.setDuration(60);
        chore.setInterval(new Interval(5, Interval.DateUnit.DAYS));
        return chore;
    }
}
