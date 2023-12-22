package akletini.life.core.shared.utils;

import akletini.life.core.chore.repository.entity.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    private DateUtils() {

    }

    public static Date localDateToDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDateTimeToDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate dateToLocalDate(Date date) {
        return LocalDate.ofInstant(
                date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(
                date.toInstant(), ZoneId.systemDefault());
    }

    public static Date addInterval(Interval interval, int intervalValue, Date date) {
        switch (interval.getUnit()) {
            case DAYS -> date = DateUtils.addDays(date, intervalValue);
            case WEEKS -> date = DateUtils.addWeeks(date, intervalValue);
            case MONTHS -> date = DateUtils.addMonths(date, intervalValue);
        }
        return date;
    }

    public static Date dateStringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date dateStringToDateTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static String dateTimeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    public static String todaysDateAsString() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }
}
