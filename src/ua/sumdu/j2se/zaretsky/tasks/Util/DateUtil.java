package ua.sumdu.j2se.zaretsky.tasks.Util;

/**
 * Created by Nikolion on 07.01.2017.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Вспомогательные функции для работы с датами.
 *
 * @author Marco Jakob
 */
public class DateUtil {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_F = new SimpleDateFormat(TIME_PATTERN);


    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_F.format(date);
    }


    public static Date parse(String dateString) {
        try {
            return DATE_F.parse(dateString);
        } catch (DateTimeParseException e) {
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Проверяет, является ли строка корректной датой.
     *
     * @param dateString
     * @return true, если строка является корректной датой
     */
    public static boolean validDate(String dateString) {
        // Пытаемся разобрать строку.
        return DateUtil.parse(dateString) != null;
    }

    public static String secondsToStringTime(int t) {
        String result = "";
        int days = t / (60 * 60 * 24);
        int hours = (t - days * 60 * 60 * 24) / (60 * 60);
        int minutes = (t - (days * 60 * 60 * 24) - (hours * 60 * 60)) / 60;
        int seconds = t - (days * 60 * 60 * 24) - (hours * 60 * 60) - (minutes * 60);

        if (days > 0) {
            result = result + days + ending(days, " day");
        }
        if (hours > 0) {
            result = result + " " + hours + ending(hours, " hour");
        }
        if (minutes > 0) {
            result = result + " " + minutes + ending(minutes, " minute");
        }
        if (seconds > 0) {
            result = result + " " + seconds + ending(seconds, " second");
        }
        result = result.trim();
        return result;
    }

    private static String ending(int time, String s) {
        if (time < 2) {
            return s;
        } else {
            return s + "s";
        }
    }

    public static int parseInterval(String intervalString) throws
            ParseException, IllegalArgumentException {
        if (intervalString == null || intervalString.isEmpty()) {
            return 0;
        }
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        String[] parts = intervalString.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Interval must contains of " +
                    "number and word");
        }
        for (int i = 0; i < parts.length; i = i + 2) {
            if (parts[i + 1].contains("day")) {
                day = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i + 1].contains("hour")) {
                hour = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i + 1].contains("minute")) {
                minute = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i + 1].contains("second")) {
                second = Integer.parseInt(parts[i]);
            }
        }
        if (day < 0 || hour < 0 || minute < 0 || second < 0) {
            throw new IllegalArgumentException("Incorrect time");
        }
        return ((day * 60 * 60 * 24) + (hour * 60 * 60) + (minute * 60) + second);
    }

    public static String choiceBoxTime(int t) {
        String time = "";
        if (t >= 0 && t < 10) {
            time = "0" + Integer.toString(t);
            return time;
        } else {
            time = Integer.toString(t);
            return time;
        }
    }

    public static LocalDate dateToLaocalDate(Date date) {
        LocalDate result;
        result = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return result;
    }

    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId
                .systemDefault()));
        Date result = Date.from(instant);
        return result;
    }


    public static Date localDateToDate(LocalDateTime localDate) {
        Date out = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
        return out;
    }

}