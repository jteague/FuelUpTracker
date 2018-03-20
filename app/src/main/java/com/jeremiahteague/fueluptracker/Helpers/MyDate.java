package com.jeremiahteague.fueluptracker.Helpers;

import android.util.Log;
import android.widget.TextView;

import com.jeremiahteague.fueluptracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeremiah on 1/29/2015.
 */
public class MyDate {

    public static String getTodayDateString() {
        Calendar cal = Calendar.getInstance();
        return getDateString(cal);
    }

    public static String getDateString(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public static String getDateString(Calendar cal) {
        return getDateString(cal.getTime());
    }

    public static String getDateString(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return getDateString(cal);
    }

    public static Calendar getCalendar(String dateStr) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

        try {
            cal.setTime(sdf.parse(dateStr));
            return cal;
        }
        catch(Exception ex) {
            return null;
        }
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public static String getThreeLeterMonth(String dateStr) {
        try {
            Calendar cal = MyDate.getCalendar(dateStr);
            return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        } catch(Exception ex) {
            dateStr = dateStr.substring(0, 3);
            return dateStr;
        }
    }

    public static String getDateNumberInMonth(String dateStr) {
        try {
            Calendar cal = MyDate.getCalendar(dateStr);
            return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        } catch(Exception ex) {
            dateStr = dateStr.substring(4, 6);
            dateStr = dateStr.replace(',', '\0');
            return dateStr;
        }
    }

    public static String getYearNumber(String dateStr) {
        try {
            Calendar cal = MyDate.getCalendar(dateStr);
            return String.valueOf(cal.get(Calendar.YEAR));
        } catch(Exception ex) {
            dateStr = dateStr.substring(7, dateStr.length());
            dateStr = dateStr.replace(',', '\0');
            return dateStr;
        }
    }
}
