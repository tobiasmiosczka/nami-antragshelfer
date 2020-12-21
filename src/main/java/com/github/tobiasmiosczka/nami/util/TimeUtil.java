package com.github.tobiasmiosczka.nami.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getDateString(LocalDate date) {
        return (date == null) ? "" : FORMATTER.format(date);
    }

    public static int calcAge(LocalDateTime birthDate, LocalDate currentDate) {
        return Period.between(LocalDate.from(birthDate), currentDate).getYears();
    }

    public static String calcAgeRange(LocalDateTime birthday, LocalDate from, LocalDate to) {
        int ageStart = calcAge(birthday, from);
        int ageEnd = calcAge(birthday, to);
        if (ageStart == ageEnd)
            return String.valueOf(ageStart);
        else
            return ageStart + "-" + ageEnd;
    }

}
