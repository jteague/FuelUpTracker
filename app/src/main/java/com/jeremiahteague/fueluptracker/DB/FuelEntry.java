package com.jeremiahteague.fueluptracker.DB;

import com.jeremiahteague.fueluptracker.Helpers.MyDate;

import java.util.Calendar;

/**
 * Created by jeremiah on 1/29/2015.
 */
public class FuelEntry {
    private long _id;
    private Calendar _cal;
    private String _location;
    private float _pricePerGallon;
    private int _odometer;
    private float _gallonsBought;
    private float _totalPrice;

    public FuelEntry() {}
    public FuelEntry(long id, Calendar cal, String location, float pricePerGallon, int odometer,
                     float gallonsBought, float totalPrice) {
        _id = id;
        _cal = cal;
        _location = location;
        _pricePerGallon = pricePerGallon;
        _odometer = odometer;
        _gallonsBought = gallonsBought;
        _totalPrice = totalPrice;
    }
    public FuelEntry(long id, Calendar cal, String location, float pricePerGallon, int odometer,
                     float gallonsBought) {
        _id = id;
        _cal = cal;
        _location = location;
        _pricePerGallon = pricePerGallon;
        _odometer = odometer;
        _gallonsBought = gallonsBought;
    }
    public FuelEntry(Calendar cal, String location, float pricePerGallon, int odometer,
                     float gallonsBought, float totalPrice) {
        _cal = cal;
        _location = location;
        _pricePerGallon = pricePerGallon;
        _odometer = odometer;
        _gallonsBought = gallonsBought;
        if (totalPrice <= 0f) {
            _totalPrice = gallonsBought * pricePerGallon;
        } else {
            _totalPrice = totalPrice;
        }
    }

    public long getId() { return _id; }
    public void setId(long id) {_id = id; }

    public Calendar getCalendar() { return _cal; }
    public void setCalendar(Calendar cal) {_cal = cal; }
    public void setCalendar(String dateStr) {
        try {
            _cal = MyDate.getCalendar(dateStr);
        }
        catch (Exception ex) { _cal = Calendar.getInstance(); }
    }

    public String getLocation() { return _location; }
    public void setLocation(String loc) { _location = loc; }

    public float getPricePerGallon() { return _pricePerGallon; }
    public void setPricePerGallon(float price) { _pricePerGallon = price; }

    public int getOdometer() { return _odometer; }
    public void setOdometer(int odometer) { _odometer = odometer; }

    public float getGallonsBought() { return _gallonsBought; }
    public void setGallonsBought(float gallonsBought) { _gallonsBought = gallonsBought; }

    public float getTotalPrice() { return _totalPrice; }
    public void setTotalPrice(float totalPrice) { _totalPrice = totalPrice; }

    public float getPricePerGallonDiff(FuelEntry other) {
        return Math.abs(_pricePerGallon - other.getPricePerGallon());
    }

    public int getOdometerDiff(FuelEntry other) {
        int toReturn = Math.abs(_odometer - other.getOdometer());
        if(toReturn == 0)
            return 1;

        return toReturn;
    }

    public float getGallonsBoughtDiff(FuelEntry other) {
        return Math.abs(_gallonsBought - other.getGallonsBought());
    }

    public float getTotalPriceDiff(FuelEntry other) {
        return Math.abs(_totalPrice - other.getTotalPrice());
    }

    public long daysBetweenFuelUp(FuelEntry other) {
        return MyDate.daysBetween(_cal, other.getCalendar());
    }

    public String getSqlInsertString() {
        String insert = DatabaseHelper.getInsertFormatString(_id, _cal, _location, _pricePerGallon, _odometer, _gallonsBought, _totalPrice);
        return insert;
    }

    @Override
    public String toString() {
        /*return "Entry:: location: " + _location + ", price per gallon: " + _pricePerGallon +
                ", gallons bought: " + _gallonsBought + ", odometer: " + _odometer;*/
        return "Entry:: Location: "+_location+", Calendar: "+MyDate.getDateString(_cal)+
                ", Price Per Gallon: "+_pricePerGallon+", Gallons Bought: "+_gallonsBought+
                ", Odometer: "+_odometer+", Total Price: "+_totalPrice;
    }
}
