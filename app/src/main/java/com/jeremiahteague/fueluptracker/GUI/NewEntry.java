package com.jeremiahteague.fueluptracker.GUI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremiahteague.fueluptracker.DB.DatabaseHelper;
import com.jeremiahteague.fueluptracker.DB.FuelEntry;
import com.jeremiahteague.fueluptracker.Helpers.Helper;
import com.jeremiahteague.fueluptracker.Helpers.MyDate;
import com.jeremiahteague.fueluptracker.Webservices.GeocoderWebserviceClient;
import com.jeremiahteague.fueluptracker.R;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEntry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEntry extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View _view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewEntry.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEntry newInstance(String param1, String param2) {
        NewEntry fragment = new NewEntry();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewEntry() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_new_entry, container, false);

        // Add the page elements and their listeners
        Button btnAddEntry = (Button) _view.findViewById(R.id.btnSave);
        btnAddEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClick_btnAddEntry(view);
            }
        });

        // GPS & Location Stuff
        Button btnGpsLookup = (Button) _view.findViewById(R.id.btnGPS);
        final EditText textBox = (EditText) _view.findViewById(R.id.txtLocation);
        final View viewToPass = _view;
        btnGpsLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btnGpsLookup(viewToPass, textBox);
            }
        });

        // Date Stuff
        final TextView txtDate = (TextView) _view.findViewById(R.id.txtDate);
        txtDate.setText(MyDate.getDateString(Calendar.getInstance()));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_datePicker();
            }
        });

        return _view;
    }

    public void onClick_btnGpsLookup(View view, EditText editText) {
        LocationManager lm = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            editText.setHint("Could not get location data");
            return;
        }
        try {
            GeocoderWebserviceClient wsClient = new GeocoderWebserviceClient(view, editText);
            wsClient.execute(location.getLatitude(), location.getLongitude(), 1d);
            //List<Address> adds = wsClient.execute(location.getLatitude(), location.getLongitude(), 5d).get(); // now let the user pick from the results
        }
        catch (Exception ex) {
            editText.setHint("Could not get address data");
        }
    }

    public void onClick_datePicker() {
        TextView txtDate = (TextView) _view.findViewById(R.id.txtDate);
        Calendar cal  = MyDate.getCalendar(txtDate.getText().toString());
        if (cal == null) cal = Calendar.getInstance();

        new DatePickerDialog(_view.getContext(),
                listener,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH)
        ).show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            String dateString = MyDate.getDateString(arg1, arg2, arg3);
            TextView txtDate = (TextView) _view.findViewById(R.id.txtDate);
            txtDate.setText(dateString);
        }
    };

    public void onClick_btnAddEntry(View v) {
        // Get the values entered
        Calendar cal;
        String location;
        float pricePerGallon;
        int odometer;
        float gallonsBought;
        float totalPrice = 0.0f;

        try {
            TextView dateTxt = (TextView) _view.findViewById(R.id.txtDate);
            cal = MyDate.getCalendar(dateTxt.getText().toString());
            if(cal == null)
                throw new Exception("Could not parse date string");
        }
        catch (Exception ex) {
            Toast.makeText(getActivity(), "Couldn't parse date", Toast.LENGTH_LONG).show();
            return;
        }
        try { location = ((EditText)_view.findViewById(R.id.txtLocation)).getText().toString(); }
        catch (Exception ex) {
            Toast.makeText(getActivity(), "Couldn't parse location", Toast.LENGTH_LONG).show();
            return;
        }
        try { pricePerGallon = Float.parseFloat(((EditText) _view.findViewById(R.id.txtPricePerGallon)).getText().toString()); }
        catch (Exception ex) {
            Toast.makeText(getActivity(), "Couldn't parse price per gallon", Toast.LENGTH_LONG).show();
            return;
        }
        try { odometer = Integer.parseInt(((EditText) _view.findViewById(R.id.txtOdometer)).getText().toString()); }
        catch (Exception ex) {
            Toast.makeText(getActivity(), "Couldn't parse odometer", Toast.LENGTH_LONG).show();
            return;
        }
        try { gallonsBought = Float.parseFloat(((EditText) _view.findViewById(R.id.txtGallonsBought)).getText().toString()); }
        catch (Exception ex) {
            Toast.makeText(getActivity(), "Couldn't parse gallons bought", Toast.LENGTH_LONG).show();
            return;
        }
        try { totalPrice = Float.parseFloat(((EditText) _view.findViewById(R.id.txtTotalPrice)).getText().toString()); }
        catch (Exception ex) {
            totalPrice = 0f;
            Toast.makeText(getActivity(), "Total Price will be calculated", Toast.LENGTH_SHORT).show();
        }

        // Add the data to the DB
        FuelEntry fuelEntry = addEntryToDb(cal, location, pricePerGallon, odometer, gallonsBought, totalPrice);

        // Clear the fields
        clearFields();

        // Pop up message for the user
        Toast.makeText(getActivity(), "Entry Added!", Toast.LENGTH_SHORT).show();

        // Remove the software keyboard
        Helper.hideKeyboard(getActivity());

        // Go to the fully loaded fuel entry
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment frag = new ViewData_Entry().newInstance(String.valueOf(fuelEntry.getId()));
        fragmentManager.beginTransaction().
                replace(R.id.container, frag)
                .addToBackStack("viewSingleFuelEntryItem")
                .commit();
    }

    private FuelEntry addEntryToDb(Calendar cal, String location, float pricePerGallon, int odometer,
                              float gallonsBought, float totalPrice) {
        FuelEntry entry = new FuelEntry(cal, location, pricePerGallon, odometer, gallonsBought, totalPrice);
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        long id = dbHelper.insertFuelEntry(entry);
        dbHelper.close();

        entry.setId(id);
        return entry;
    }

    private void clearFields() {
        ((TextView) _view.findViewById(R.id.txtDate)).setText(MyDate.getTodayDateString());
        ((TextView) _view.findViewById(R.id.txtGallonsBought)).setText("");
        ((TextView) _view.findViewById(R.id.txtLocation)).setText("");
        ((TextView) _view.findViewById(R.id.txtLocation)).setHint("");
        ((TextView) _view.findViewById(R.id.txtPricePerGallon)).setText("");
        ((TextView) _view.findViewById(R.id.txtOdometer)).setText("");
        ((TextView) _view.findViewById(R.id.txtTotalPrice)).setText("");
        ((TextView) _view.findViewById(R.id.txtTotalPrice)).setHint("optional");
    }
}