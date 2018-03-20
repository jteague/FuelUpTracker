package com.jeremiahteague.fueluptracker;

import android.test.InstrumentationTestCase;
import com.jeremiahteague.fueluptracker.Helpers.MyDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jeremiah on 1/31/2015.
 */
public class MyDateTest extends InstrumentationTestCase {

    public void test_getCalendar() {
        String calStr = "Nov 18, 1978";
        Calendar cal = MyDate.getCalendar(calStr);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), 18);
        assertEquals(cal.get(Calendar.MONTH), 10); // because java is weird
        assertEquals(cal.get(Calendar.YEAR), 1978);

        calStr = "Feb 25, 2015";
        cal = MyDate.getCalendar(calStr);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), 25);
        assertEquals(cal.get(Calendar.MONTH), 1); // because java is weird
        assertEquals(cal.get(Calendar.YEAR), 2015);
    }

    public void test_getDateStringDate() {
        Date date = new Date(1978 - 1900, 10, 18); // because java is weird
        String dateStr = MyDate.getDateString(date);
        assertEquals(dateStr, "Nov 18, 1978");
    }

    public void test_getDateStringCal() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1978);
        cal.set(Calendar.MONTH, 10); // because java is weird
        cal.set(Calendar.DAY_OF_MONTH, 18);
        String calStr = MyDate.getDateString(cal);
        assertEquals(calStr, "Nov 18, 1978");
    }

    public void test_getThreeLetterMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1978);
        cal.set(Calendar.MONTH, 10); // because java is weird
        cal.set(Calendar.DAY_OF_MONTH, 18);
        String monStr = MyDate.getThreeLeterMonth("Nov 18, 1978");
        assertEquals(monStr, "Nov");
    }

    public void test_getDateNumberInMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1978);
        cal.set(Calendar.MONTH, 10); // because java is weird
        cal.set(Calendar.DAY_OF_MONTH, 18);
        String dateStr = "Nov 18, 1978";
        String dateNumStr = MyDate.getDateNumberInMonth(dateStr);
        assertEquals(dateNumStr, "18");

        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1978);
        cal.set(Calendar.MONTH, 10); // because java is weird
        cal.set(Calendar.DAY_OF_MONTH, 8);
        dateStr = "Nov 18, 1978";
        dateNumStr = MyDate.getDateNumberInMonth(dateStr);
        assertEquals(dateNumStr, "8");
    }
}
