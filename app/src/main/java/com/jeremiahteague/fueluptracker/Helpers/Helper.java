package com.jeremiahteague.fueluptracker.Helpers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigDecimal;

/**
 * Created by jeremiah on 2/21/2015.
 */
public class Helper {

    public static final String AVERAGE_DAYS_BETWEEN_KEY = "daysBetween";
    public static final String AVERAGE_FUELUP_COST_KEY = "fuelUpCost";
    public static final String AVERAGE_GALLONS_BOUGHT_KEY = "gallonsBought";
    public static final String AVERAGE_PRICE_PER_GALLON_KEY = "pricePerGallon";
    public static final String AVERAGE_MILES_DRIVEN_KEY = "milesDriven";
    public static final String AVERAGE_COST_PER_MILE_KEY = "costPerMile";
    public static final String AVERAGE_MILES_PER_GALLON_KEY = "milesPerGallon";
    public static final String AVERAGE_GAS_STATION_USED_KEY = "gasStationUsed";

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static BigDecimal round(BigDecimal bd, int decimalPlace) {
        return bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    }

    public static void hideKeyboard(FragmentActivity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
