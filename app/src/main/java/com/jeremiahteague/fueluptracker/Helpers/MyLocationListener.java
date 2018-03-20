package com.jeremiahteague.fueluptracker.Helpers;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by jeremiah on 2/22/2015.
 */
public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}