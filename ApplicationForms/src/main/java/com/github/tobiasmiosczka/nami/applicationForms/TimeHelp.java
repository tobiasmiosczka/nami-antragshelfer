package com.github.tobiasmiosczka.nami.applicationForms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelp {
    private static final SimpleDateFormat dfOutput = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat dfYearOutput = new SimpleDateFormat("yyyy");

    public static String getDateYearString(Date date) {
        if(date == null) {
            return "";
        }
        return dfYearOutput.format(date);
    }

    public static String getDateString(Date date) {
        if(date == null)
            return "";
        return dfOutput.format(date);
    }

    public static Date getDateFromInputInput(String date) throws ParseException {
        return dfOutput.parse(date);
    }

    public static int calcAge(Date birthday, Date date) {
        if (birthday == null || date == null) {
            return 0;
        }
        return (int)Math.floor((date.getTime() - birthday.getTime()) / (1000 * 60 * 60 * 24 * 365.242));
    }

    public static String calcAgeRange(Date birthday, Date from, Date to) {
        //compute age
        int diffInYearsStart = calcAge(birthday, from);
        int diffInYearsEnd = calcAge(birthday, to);
        if (diffInYearsEnd > diffInYearsStart) {
            //participant has his/her birthday at the event
            return String.valueOf(diffInYearsStart) + "-" + String.valueOf(diffInYearsEnd);
        } else {
            return String.valueOf(diffInYearsStart);
        }
    }
}
