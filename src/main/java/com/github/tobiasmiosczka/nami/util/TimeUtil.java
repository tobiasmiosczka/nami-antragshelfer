package com.github.tobiasmiosczka.nami.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getDateYearString(LocalDateTime date) {
        if (date == null)
            return "";
        return String.valueOf(date.getYear());
    }

    public static String getDateString(LocalDate date) {
        if(date == null)
            return "";
        return DATE_TIME_FORMATTER.format(date);
    }

    public static int calcAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

    public static String calcAgeRange(LocalDate birthday, LocalDate from, LocalDate to) {
        //compute age
        int diffInYearsStart = calcAge(birthday, from);
        int diffInYearsEnd = calcAge(birthday, to);
        if (diffInYearsEnd > diffInYearsStart) {
            //participant has his/her birthday at the event
            return diffInYearsStart + "-" + diffInYearsEnd;
        } else {
            return String.valueOf(diffInYearsStart);
        }
    }

}
