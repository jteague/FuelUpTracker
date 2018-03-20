package com.jeremiahteague.fueluptracker;

import android.test.InstrumentationTestCase;

import com.jeremiahteague.fueluptracker.DB.FuelEntry;

import java.util.Calendar;

/**
 * Created by jeremiah on 1/31/2015.
 */
public class fuelEntryTest extends InstrumentationTestCase {

    public void test_toString() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 18);
        cal.set(Calendar.MONTH, 10); // Nov, because java is weird
        cal.set(Calendar.YEAR, 1978);

        FuelEntry entry = new FuelEntry(0, cal, "home",
                1.89f, 100000, 11.125f, 21.03f);
        String entryStrMaster = "Entry:: Location: home, Calendar: Nov 18, 1978, Price Per Gallon: 1.89" +
                ", Gallons Bought: 11.125, Odometer: 100000";
        assertEquals(entryStrMaster, entry.toString());
    }

    public void test_setCalender() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 18);
        cal.set(Calendar.MONTH, 10); // Nov, because java is weird
        cal.set(Calendar.YEAR, 1978);

        FuelEntry entry1 = new FuelEntry();
        entry1.setCalendar(cal);
        FuelEntry entry2 = new FuelEntry();
        entry2.setCalendar("Nov 18, 1978");
        assertEquals(entry1.getCalendar().get(Calendar.DAY_OF_MONTH), entry2.getCalendar().get(Calendar.DAY_OF_MONTH));
        assertEquals(entry1.getCalendar().get(Calendar.MONTH), entry2.getCalendar().get(Calendar.MONTH));
        assertEquals(entry1.getCalendar().get(Calendar.YEAR), entry2.getCalendar().get(Calendar.YEAR));
    }
}
