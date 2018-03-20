package com.jeremiahteague.fueluptracker.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.jeremiahteague.fueluptracker.R;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by jeremiah on 3/8/2015.
 */
public class FuelEntryCursorAdapter extends SimpleCursorAdapter {

    public FuelEntryCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if (v.getId() == R.id.listView_Mon) {
            text = MyDate.getThreeLeterMonth(text);
        }

        if (v.getId() == R.id.listView_DayNum) {
            text = MyDate.getDateNumberInMonth(text);
        }

        if (v.getId() == R.id.listView_year) {
            text = MyDate.getYearNumber(text);
        }

        if (v.getId() == R.id.listView_odometer) {
            text = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(text));
        }

        v.setText(text);
    }
}
