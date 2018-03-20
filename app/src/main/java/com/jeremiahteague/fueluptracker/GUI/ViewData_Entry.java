package com.jeremiahteague.fueluptracker.GUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremiahteague.fueluptracker.DB.DatabaseHelper;
import com.jeremiahteague.fueluptracker.DB.FuelEntry;
import com.jeremiahteague.fueluptracker.Helpers.Helper;
import com.jeremiahteague.fueluptracker.Helpers.MyDate;
import com.jeremiahteague.fueluptracker.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;


public class ViewData_Entry extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int _fuelEntryId;
    private View _view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ViewData_Entry.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewData_Entry newInstance(String param1) {
        ViewData_Entry fragment = new ViewData_Entry();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewData_Entry() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            _fuelEntryId = Integer.parseInt(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_view_data_entry, container, false);

        populateTextBoxes();
        setupButtons();

        return _view;
    }

    private void populateTextBoxes() {
        // Get the objects
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        FuelEntry fuelEntry = dbHelper.getFuelEntryById(_fuelEntryId);
        FuelEntry fuelEntryPrevious = dbHelper.getPreviousFuelEntry(fuelEntry.getOdometer());

        // Get the text boxes
        TextView txtDate = (TextView) _view.findViewById(R.id.entry_txtDate);
        TextView txtLocation = (TextView) _view.findViewById(R.id.entry_txtLocation);
        TextView txtPricePerGallon = (TextView) _view.findViewById(R.id.entry_txtPricePerGallon);
        TextView txtOdometer = (TextView) _view.findViewById(R.id.entry_txtOdometer);
        TextView txtGallonsBought = (TextView) _view.findViewById(R.id.entry_txtGallonsBought);
        TextView txtTotalPrice = (TextView) _view.findViewById(R.id.entry_txtTotalPrice);
        TextView txtDistanceDriven = (TextView) _view.findViewById(R.id.entry_lblDistanceDrivenValue);
        TextView txtPricePerDistanceUnit = (TextView) _view.findViewById(R.id.entry_lblPricePerDistanceUnitValue);
        TextView txtDistancePerGallon = (TextView) _view.findViewById(R.id.entry_lblDistanceUnitPerGallonValue);
        TextView txtNumDaysSinceFillUp = (TextView) _view.findViewById(R.id.entry_lblDaysSinceFillUpValue);

        // Populate the text boxes with the values we know
        txtDate.setText(MyDate.getDateString(fuelEntry.getCalendar()));
        txtLocation.setText(fuelEntry.getLocation());
        txtPricePerGallon.setText(String.valueOf(fuelEntry.getPricePerGallon()));
        txtOdometer.setText(String.valueOf(fuelEntry.getOdometer()));
        txtGallonsBought.setText(String.valueOf(fuelEntry.getGallonsBought()));
        txtTotalPrice.setText(String.valueOf(Helper.round(fuelEntry.getTotalPrice(), 2)));

        if(fuelEntryPrevious == null) {
            // There is no previous entry
            txtDistanceDriven.setText("No previous entry for calculation");
            txtPricePerDistanceUnit.setText("No previous entry for calculation");
            txtDistancePerGallon.setText("No previous entry for calculation");
            txtNumDaysSinceFillUp.setText("No previous entry for calculation");
        }
        else {
            // Calculate the "interesting" data for this fill up
            // TODO: add unit settings, money settings, etc.
            int milesDriven = fuelEntry.getOdometer() - fuelEntryPrevious.getOdometer();
            BigDecimal costPerMile = Helper.round(fuelEntry.getTotalPrice() / milesDriven, 3);
            BigDecimal milesPerGallon = Helper.round(milesDriven / fuelEntry.getGallonsBought(), 2);
            long daysBetween = fuelEntry.daysBetweenFuelUp(fuelEntryPrevious);

            txtDistanceDriven.setText(milesDriven + " miles driven");
            txtPricePerDistanceUnit.setText("$" + costPerMile + " per mile");
            txtDistancePerGallon.setText(milesPerGallon + " MPG");
            txtNumDaysSinceFillUp.setText(daysBetween + " days since last fill-up");
        }
    }

    private FuelEntry getCurrentFuelEntryObj() {
        // Get the text boxes
        TextView txtDate = (TextView) _view.findViewById(R.id.entry_txtDate);
        TextView txtLocation = (TextView) _view.findViewById(R.id.entry_txtLocation);
        TextView txtPricePerGallon = (TextView) _view.findViewById(R.id.entry_txtPricePerGallon);
        TextView txtOdometer = (TextView) _view.findViewById(R.id.entry_txtOdometer);
        TextView txtGallonsBought = (TextView) _view.findViewById(R.id.entry_txtGallonsBought);
        TextView txtTotalPrice = (TextView) _view.findViewById(R.id.entry_txtTotalPrice);

        // make a new obj with the current values in the text boxes
        FuelEntry fuelEntry = new FuelEntry(
                _fuelEntryId,
                MyDate.getCalendar(txtDate.getText().toString()),
                txtLocation.getText().toString(),
                Float.parseFloat(txtPricePerGallon.getText().toString()),
                Integer.parseInt(txtOdometer.getText().toString()),
                Float.parseFloat(txtGallonsBought.getText().toString()),
                Float.parseFloat(txtTotalPrice.getText().toString()));

        return fuelEntry;
    }

    private void setupButtons() {
        // Save button
        Button btnSaveEntry = (Button) _view.findViewById(R.id.entry_btnSave);
        btnSaveEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                dbHelper.updateFuelEntry(getCurrentFuelEntryObj());
                switchToViewDataFragment();
                Toast.makeText(getActivity(), "Entry saved", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button
        Button btnDeleteEntry = (Button) _view.findViewById(R.id.entry_btnDelete);
        btnDeleteEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClick_deleteSingleEntry(view);
            }
        });
    }

    public void onClick_deleteSingleEntry(View view) {
        final View vView = view; // final for the alert box

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()).
                setTitle("Delete entry?").
                setMessage("Are you sure you want to delete this entry?").
                setNegativeButton("No", null).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                        dbHelper.deleteFuelEntry(_fuelEntryId);
                        switchToViewDataFragment();
                        Toast.makeText(getActivity(), "Entry deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    private void switchToViewDataFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment frag = new ViewData().newInstance("view data", "");
        fragmentManager.beginTransaction().
                replace(R.id.container, frag).
                addToBackStack("viewData").
                commit();

        // Remove the software keyboard
        Helper.hideKeyboard(getActivity());
    }
}
