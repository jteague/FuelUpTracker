package com.jeremiahteague.fueluptracker.Webservices;

import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.ListPopupWindow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jeremiah on 1/25/2015.
 */
public class GeocoderWebserviceClient extends AsyncTask<Double, Void, List<Address>>
{
    private static final String STATUS_OK = "OK";
    protected EditText _editText;
    protected View _view;

    public GeocoderWebserviceClient(View view, EditText editText) {
        _view = view;
        _editText = editText;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        super.onPostExecute(addresses);

        try {
            if (_editText != null) {
                _editText.setText(addresses.get(0).getFeatureName());
            }
        }
        catch (Exception ex) {
            _editText.setHint("Could not get address data");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Address> doInBackground(Double... args) {

        double latitude = args[0];
        double longitude = args[1];
        int maxResults = (int) Math.round(args[2]);

        InputStream is = null;
        ByteArrayOutputStream os = null;

        try {
            final URL u = new URL(getUrlForLookup(latitude, longitude));
            final URLConnection connection = u.openConnection();
            connection.connect();

            is = connection.getInputStream();
            os = new ByteArrayOutputStream();

            final byte[] buffer = new byte[5120];
            int read;

            while (true) {
                read = is.read(buffer, 0, buffer.length);
                if (read == -1)
                    break;
                os.write(buffer, 0, read);
            }

            byte[] byteArray = os.toByteArray();
            return getAddressesFromByteArray(byteArray, maxResults);
        }
        catch (IOException e) { e.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }
        finally {
            if (is != null)
                try { is.close(); }
                catch (IOException ignored) {}
            if (os != null)
                try { os.close(); }
                catch (IOException ignored) {}
        }
        return null;
    }

    private List<Address> getAddressesFromByteArray(byte[] data, int maxResults) {

        final List<Address> results = new ArrayList<Address>();

        if (data != null) {
            try {
                this.parseJson(results, maxResults, data);
                return results;
            }
            catch (Exception ex) {}
        }
        return null;
    }

    private void parseJson(List<Address> address, int maxResults, byte[] data)
    {
        Log.d("GeocoderWebserviceClient", "GeocoderWebserviceClient::parseJson::in method");
        try {
            final String json = new String(data, "UTF-8");
            final JSONObject o = new JSONObject(json);
            final String status = o.getString("status");
            if (status.equals(STATUS_OK)) {
                Log.d("GeocoderWebserviceClient","GeocoderWebserviceClient::parseJson::status is OK");
                final JSONArray a = o.getJSONArray("results");

                for (int i = 0; i < maxResults && i < a.length(); i++) {
                    final Address current = new Address(Locale.getDefault());
                    final JSONObject item = a.getJSONObject(i);

                    current.setFeatureName(item.getString("formatted_address"));
                    final JSONObject location = item.getJSONObject("geometry").getJSONObject("location");
                    current.setLatitude(location.getDouble("lat"));
                    current.setLongitude(location.getDouble("lng"));

                    address.add(current);
                }

            }
        }
        catch (Exception e) {

        }
    }

    public String getUrlForLookup(double latitude, double longitude)
            throws IOException
    {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("latitude == " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("longitude == " + longitude);
        }

        final StringBuilder url = new StringBuilder(
                "http://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=");
        url.append(latitude);
        url.append(',');
        url.append(longitude);
        url.append("&language=");
        url.append(Locale.getDefault().getLanguage());
        return url.toString();
    }

}
