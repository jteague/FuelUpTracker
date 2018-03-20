package com.jeremiahteague.fueluptracker.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jeremiahteague.fueluptracker.Helpers.Helper;
import com.jeremiahteague.fueluptracker.Helpers.MyDate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jeremiah on 1/29/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // <editor-fold desc="Database Schema">
    // DB info
    private static final String DATABASE_NAME = "fuel_up_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table info
    // Be sure to add info to final string, getAllTables() and onCreate()
    // TODO: find a better way to do this
    public static final String TABLE_FUEL_ENTRIES = "fuel_entry";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_PRICE_PER_GALLON = "price_per_gallon";
    public static final String COLUMN_ODOMETER = "odometer";
    public static final String COLUMN_GALLONS_BOUGHT = "gallons_bought";
    public static final String COLUMN_TOTAL_PRICE = "total_price";

    public static final int COLUMN_ID_ID = 0;
    public static final int COLUMN_ID_DATE = 1;
    public static final int COLUMN_ID_LOCATION = 2;
    public static final int COLUMN_ID_PRICE_PER_GALLON = 3;
    public static final int COLUMN_ID_ODEMETER = 4;
    public static final int COLUMN_ID_GALLONS_BOUGHT = 5;
    public static final int COLUMN_ID_TOTAL_PRICE = 6;

    public String[] allColumns = {
            COLUMN_ID,
            COLUMN_DATE,
            COLUMN_LOCATION,
            COLUMN_PRICE_PER_GALLON,
            COLUMN_ODOMETER,
            COLUMN_GALLONS_BOUGHT,
            COLUMN_TOTAL_PRICE
    };


    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_FUEL_ENTRIES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_DATE + " TEXT NOT NULL,"
            + COLUMN_LOCATION + " TEXT NOT NULL,"
            + COLUMN_PRICE_PER_GALLON + " REAL NOT NULL,"
            + COLUMN_ODOMETER + " INTEGER NOT NULL,"
            + COLUMN_GALLONS_BOUGHT + " REAL NOT NULL,"
            + COLUMN_TOTAL_PRICE + " REAL"
            + ");";

    public static String getInsertFormatString(long id, Calendar cal, String location, float pricePerGallon,
                                               int odometer, float gallonsBought, float totalPrice) {
        // Add quotes around text strings
        String quoteChar = "'";
        String dateStr = MessageFormat.format("{1}{0}{1}", MyDate.getDateString(cal), quoteChar);
        String locationStr = MessageFormat.format("{1}{0}{1}", location, quoteChar);

        String insert = MessageFormat.format("INSERT INTO {0} ({1},{2},{3},{4},{5},{6},{7}) VALUES ({8},{9},{10},{11},{12},{13},{14});",
                DatabaseHelper.TABLE_FUEL_ENTRIES, DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_LOCATION, DatabaseHelper.COLUMN_PRICE_PER_GALLON,
                DatabaseHelper.COLUMN_ODOMETER, DatabaseHelper.COLUMN_GALLONS_BOUGHT, DatabaseHelper.COLUMN_TOTAL_PRICE,
                Long.toString(id), dateStr, locationStr, pricePerGallon, Long.toString(odometer), gallonsBought, totalPrice);
        return insert;
    }

    public void runSqlCmd(String sqlCmd)
    {
        SQLiteDatabase database = getWritableDatabase();

        String[] commands = sqlCmd.split("\n");

        for (int i = 0; i < commands.length; i++) {
            String curCmd = commands[i];
            database.execSQL(curCmd);
        }

        database.close();
    }

    protected String[] getAllTables() { return new String[] { TABLE_FUEL_ENTRIES }; }

    //</editor-fold>

    // <editor-fold desc="Implemented Methods">
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        for(int i=0; i < getAllTables().length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + getAllTables()[i]);
        }
        onCreate(db);
    }
    // </editor-fold>

    // <editor-fold desc="Insert Methods">
    public long insertFuelEntry(FuelEntry entry) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = getContentValuesNoId(entry);
        long insertId = database.insert(TABLE_FUEL_ENTRIES, COLUMN_DATE, contentValues);
        database.close();

        return insertId;
    }
    // </editor-fold>

    // <editor-fold desc="Update Methods">

    public void updateFuelEntry(FuelEntry fuelEntry) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_FUEL_ENTRIES, getContentValuesNoId(fuelEntry), COLUMN_ID + " = " + fuelEntry.getId(), null);
        db.close();
    }

    // </editor-fold>

    // <editor-fold desc="Delete Methods">

    public void deleteFuelEntry(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FUEL_ENTRIES, COLUMN_ID + "=?", new String [] {String.valueOf(id)});
        db.close();
    }

    public void deleteAllFuelEntries() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FUEL_ENTRIES, null, null);
        db.close();
    }

    // </editor-fold>

    // <editor-fold desc="Select Methods">

    public int getFuelEntryCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_ENTRIES, null);
        int x = cur.getCount();
        cur.close();
        db.close();
        return x;
    }

    public Cursor getAllFuelEntriesCursor(String orderBy, boolean desc) {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_FUEL_ENTRIES + " ORDER BY " + orderBy;
        if(desc) { query += " DESC"; }

        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public List<FuelEntry> getAllFuelEntries() {
        Cursor cursor = getAllFuelEntriesCursor(COLUMN_ODOMETER, true);
        List<FuelEntry> entries = new ArrayList<>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor != null && entries != null) {
                entries.add(cursorToFuelEntry(cursor));
                cursor.moveToNext();
            }
        }

        cursor.close();
        this.close();
        return entries;
    }

    public FuelEntry getFuelEntryById(long id) {
        FuelEntry fe = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_ENTRIES + " WHERE " + COLUMN_ID + " = " + id, null);
        cur.moveToFirst();
        if (cur.isAfterLast() == false) {
            fe = cursorToFuelEntry(cur);
        }
        cur.close();
        db.close();
        return fe;
    }

    /**
     * Gets the FuelEntry that has the highest odometer below
     * the input threshold.
     * @param upperLimit
     * @return
     */
    public FuelEntry getPreviousFuelEntry(int upperLimit) {
        FuelEntry fe = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_FUEL_ENTRIES +
                " WHERE " + COLUMN_ODOMETER + " < " + upperLimit +
                " ORDER BY " + COLUMN_ODOMETER + " DESC LIMIT 1"
                , null);
        cur.moveToFirst();
        if (cur.isAfterLast() == false) {
            fe = cursorToFuelEntry(cur);
        }
        cur.close();
        db.close();
        return fe;
    }

    public String mostCommonGasStation() {
        FuelEntry fe = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String commonLocation = "unknown";
        String query = "SELECT " + COLUMN_LOCATION + ", COUNT(" + COLUMN_LOCATION + ") AS cnt" +
                " FROM " + TABLE_FUEL_ENTRIES +
                " GROUP BY " + COLUMN_LOCATION +
                " ORDER BY cnt DESC LIMIT 1";
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();
        if (cur.isAfterLast() == false) {
            commonLocation = cur.getString(0);
            commonLocation += " ("+ cur.getInt(1) +")";
        }
        cur.close();
        db.close();
        return commonLocation;
    }

    public BigDecimal getMinOfColumn(String COLUMN, int decimalPlaces) {
        double value = 0d;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT MIN(" + COLUMN + ") FROM " + TABLE_FUEL_ENTRIES, null);
        cur.moveToFirst();
        if (cur.isAfterLast() == false) {
            String str = cur.getString(0);
            value = Double.parseDouble(str);
        }
        cur.close();
        db.close();
        return Helper.round(BigDecimal.valueOf(value), decimalPlaces);
    }

    public BigDecimal getMaxOfColumn(String COLUMN, int decimalPlaces) {
        double value = 0d;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT MAX(" + COLUMN + ") FROM " + TABLE_FUEL_ENTRIES, null);
        cur.moveToFirst();
        if (cur.isAfterLast() == false) {
            String str = cur.getString(0);
            value = Double.parseDouble(str);
        }
        cur.close();
        db.close();
        return Helper.round(BigDecimal.valueOf(value), decimalPlaces);
    }

    public List<Calendar> getAllDates() {
        List<Calendar> dates = new ArrayList<Calendar>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_DATE + " FROM " + TABLE_FUEL_ENTRIES + " ORDER BY " + COLUMN_DATE;
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();
        while(cur.isAfterLast() == false) {
            String dateStr = cur.getString(0);
            Calendar date = MyDate.getCalendar(dateStr);
            dates.add(date);
            cur.moveToNext();
        }
        cur.close();
        db.close();
        Collections.sort(dates, new Comparator<Calendar>() {
            public int compare(Calendar cal1, Calendar cal2) {
                return cal1.compareTo(cal2);
            }
        });
        return dates;
    }

    public List<Integer> getAllOdometer(boolean desc) {
        List<Integer> odometers = new ArrayList<Integer>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ODOMETER + " FROM " + TABLE_FUEL_ENTRIES + " ORDER BY " + COLUMN_ODOMETER;
        if (desc) query += " DESC";
        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();

        while(cur.isAfterLast() == false) {
            int odometer = cur.getInt(0);
            odometers.add(odometer);
            cur.moveToNext();
        }

        cur.close();
        db.close();
        return odometers;
    }

    public List<Float> getAllMilesPerGallonEntries(boolean desc) {
        List<Float> mpgs = new ArrayList<Float>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ODOMETER + ", " + COLUMN_GALLONS_BOUGHT +
                " FROM " + TABLE_FUEL_ENTRIES + " ORDER BY " + COLUMN_ODOMETER;
        if (desc) query += " DESC";
        Cursor cur = db.rawQuery(query, null);
        if (cur.getCount() < 2) return null;

        cur.moveToFirst();
        int prevOdometer = cur.getInt(0);;
        cur.moveToNext();
        while(cur.isAfterLast() == false) {
            int curOdometer = cur.getInt(0);
            float curGallonsBought = cur.getFloat(1);
            int milesDriven = curOdometer - prevOdometer;
            float mpg = milesDriven / curGallonsBought;
            mpgs.add(mpg);

            prevOdometer = curOdometer;
            cur.moveToNext();
        }

        cur.close();
        db.close();
        return mpgs;
    }

    public List<Float> getAllCostsPerMilesEntries(boolean desc) {
        List<Float> cpms = new ArrayList<Float>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ODOMETER + ", " + COLUMN_TOTAL_PRICE +
                " FROM " + TABLE_FUEL_ENTRIES + " ORDER BY " + COLUMN_ODOMETER;
        if (desc) query += " DESC";
        Cursor cur = db.rawQuery(query, null);
        if (cur.getCount() < 2) return null;

        cur.moveToFirst();
        int prevOdometer = cur.getInt(0);;
        cur.moveToNext();
        while(cur.isAfterLast() == false) {
            int curOdometer = cur.getInt(0);
            float price = cur.getFloat(1);
            int milesDriven = curOdometer - prevOdometer;
            float cpm = price / milesDriven;
            cpms.add(cpm);

            prevOdometer = curOdometer;
            cur.moveToNext();
        }

        cur.close();
        db.close();
        return cpms;
    }

    // </editor-fold>

    // <editor-fold desc="Calculation Methods">

    public List<Long> getAllNumDaysBetween() {
        List<Calendar> cals = getAllDates();

        if(cals == null || cals.size() < 2) {
            return null;
        }

        List<Long> daysBetween = new ArrayList<Long>();
        for(int i=1; i < cals.size(); i++) {
            long days = MyDate.daysBetween(cals.get(i-1), cals.get(i));
            daysBetween.add(days);
        }

        return daysBetween;
    }

    public List<Integer> getAllBetweenOdometers() {
        List<Integer> odometers = getAllOdometer(false);
        if(odometers.size() < 2) {
            return null;
        }

        List<Integer> betweenOdo = new ArrayList<Integer>();
        for(int i=1; i < odometers.size(); i++) {
            int odometer = odometers.get(i) - odometers.get(i-1);
            betweenOdo.add(odometer);
        }

        return betweenOdo;
    }

    public HashMap averagesBetweenFuelUps() {
        if(getFuelEntryCount() <= 1) { return null; } // not enough entries to calculate

        HashMap<String, BigDecimal> hashMap = new HashMap<>();
        List<FuelEntry> fuelEntries = getAllFuelEntries();
        FuelEntry feOne = null;
        FuelEntry feTwo = null;

        float daysSum = 0;              //between-based
        float fuelUpCostSum = 0;        //entry-based
        float gallonsBoughtSum = 0;     //entry-based
        float pricePerGallonSum = 0;    //entry-based
        float milesDrivenSum = 0;       //between-based
        float costPerMileSum = 0;       //between-based
        float milesPerGallonSum = 0;    //between-based

        for(int i=0; i < fuelEntries.size(); i++) {
            if(i == 0) {
                // Only work with entry-based sums on this first iteration
                // (these don't require a previous entry to calculate)
                feOne = fuelEntries.get(i);
                fuelUpCostSum += feOne.getTotalPrice();
                gallonsBoughtSum += feOne.getGallonsBought();
                pricePerGallonSum += feOne.getPricePerGallon();
            }
            else {
                feOne = fuelEntries.get(i-1);
                feTwo = fuelEntries.get(i);

                fuelUpCostSum += feTwo.getTotalPrice();
                gallonsBoughtSum += feTwo.getGallonsBought();
                pricePerGallonSum += feTwo.getPricePerGallon();

                // These sums require knowledge of the previous fuel-up to calculate
                daysSum += (float) MyDate.daysBetween(feOne.getCalendar(), feTwo.getCalendar());
                milesDrivenSum += feTwo.getOdometerDiff(feOne);
                costPerMileSum += feTwo.getTotalPrice() / (float)feTwo.getOdometerDiff(feOne);
                milesPerGallonSum += (float)feTwo.getOdometerDiff(feOne) / feTwo.getGallonsBought();
            }
        }

        // Populate the hashmap
        hashMap.put(Helper.AVERAGE_DAYS_BETWEEN_KEY, Helper.round((daysSum / (fuelEntries.size() - 1)), 2));
        hashMap.put(Helper.AVERAGE_MILES_DRIVEN_KEY, Helper.round((milesDrivenSum / (fuelEntries.size() - 1)), 2));
        hashMap.put(Helper.AVERAGE_COST_PER_MILE_KEY, Helper.round((costPerMileSum / (fuelEntries.size() - 1)), 2));
        hashMap.put(Helper.AVERAGE_MILES_PER_GALLON_KEY, Helper.round((milesPerGallonSum / (fuelEntries.size() - 1)), 2));

        hashMap.put(Helper.AVERAGE_FUELUP_COST_KEY, Helper.round((fuelUpCostSum / (fuelEntries.size())), 2));
        hashMap.put(Helper.AVERAGE_GALLONS_BOUGHT_KEY, Helper.round((gallonsBoughtSum / (fuelEntries.size())), 2));
        hashMap.put(Helper.AVERAGE_PRICE_PER_GALLON_KEY, Helper.round((pricePerGallonSum / (fuelEntries.size())), 2));

        return hashMap;
    }

    // </editor-fold>

    // <editor-fold desc="Helper Methods">

    public FuelEntry cursorToFuelEntry(Cursor cursor) {
        FuelEntry entry = new FuelEntry();
        entry.setId(cursor.getLong(COLUMN_ID_ID));
        entry.setCalendar(cursor.getString(COLUMN_ID_DATE));
        entry.setLocation(cursor.getString(COLUMN_ID_LOCATION));
        entry.setPricePerGallon(cursor.getFloat(COLUMN_ID_PRICE_PER_GALLON));
        entry.setOdometer(cursor.getInt(COLUMN_ID_ODEMETER));
        entry.setGallonsBought(cursor.getFloat(COLUMN_ID_GALLONS_BOUGHT));
        entry.setTotalPrice(cursor.getFloat(COLUMN_ID_TOTAL_PRICE));

        return entry;
    }

    private ContentValues getContentValuesNoId(FuelEntry entry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, MyDate.getDateString(entry.getCalendar()));
        contentValues.put(COLUMN_LOCATION, entry.getLocation());
        contentValues.put(COLUMN_PRICE_PER_GALLON, entry.getPricePerGallon());
        contentValues.put(COLUMN_ODOMETER, entry.getOdometer());
        contentValues.put(COLUMN_GALLONS_BOUGHT, entry.getGallonsBought());
        contentValues.put(COLUMN_TOTAL_PRICE, entry.getTotalPrice());

        return contentValues;
    }

    // </editor-fold>

}
