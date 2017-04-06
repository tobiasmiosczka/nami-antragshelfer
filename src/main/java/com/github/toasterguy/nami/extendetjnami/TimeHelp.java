package com.github.toasterguy.nami.extendetjnami;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelp {
    private static final SimpleDateFormat dfNaMi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public static Date getDateFromNaMiTimestamp(String timestamp) {
        Date date;
        try {
            date = dfNaMi.parse(timestamp);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static int calcAge(Date birthday, Date date) {
        if (birthday == null || date == null) {
            return 0;
        }
        return (int)Math.floor((date.getTime() - birthday.getTime()) / (1000 * 60 * 60 * 24 * 365.242));
    }
}
