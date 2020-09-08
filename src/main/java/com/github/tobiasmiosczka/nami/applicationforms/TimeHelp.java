package com.github.tobiasmiosczka.nami.applicationforms;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeHelp {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final SimpleDateFormat dfYearOutput = new SimpleDateFormat("yyyy");

    public static String getDateYearString(Date date) {
        return getDateYearString(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public static String getDateYearString(LocalDate date) {
        if(date == null) {
            return "";
        }
        return dfYearOutput.format(date);
    }

    public static String getDateString(LocalDate date) {
        if(date == null)
            return "";
        return DATE_TIME_FORMATTER.format(date);
    }

    public static String getDateString(Date date) {
        if(date == null)
            return "";
        return DATE_TIME_FORMATTER.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public static LocalDate getDateFromInputInput(String dateAsString) {
        return LocalDate.parse(dateAsString, DATE_TIME_FORMATTER);
    }

    public static int calcAge(Date birthdate, LocalDate currentDate) {
        return calcAge(birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), currentDate);
    }

    public static int calcAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

    public static String calcAgeRange(Date birthday, LocalDate from, LocalDate to) {
        return calcAgeRange(
                birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                from,
                to
        );
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
