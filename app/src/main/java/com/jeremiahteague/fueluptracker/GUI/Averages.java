package com.jeremiahteague.fueluptracker.GUI;

import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremiahteague.fueluptracker.DB.DatabaseHelper;
import com.jeremiahteague.fueluptracker.Helpers.Helper;
import com.jeremiahteague.fueluptracker.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Averages extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View _view;
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Averages.
     */
    // TODO: Rename and change types and number of parameters
    public static Averages newInstance(String param1, String param2) {
        Averages fragment = new Averages();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Averages() {
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
        _view = inflater.inflate(R.layout.fragment_averages, container, false);

        populateTextBoxes();

        return _view;
    }

    private void populateTextBoxes() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        int entries = dbHelper.getFuelEntryCount();
        if(entries < 2) { return; } // don't crash

        populateAverageTextBoxes(dbHelper);
        populateMinTextBoxes(dbHelper);
        populateMaxTextBoxes(dbHelper);
    }

    private void populateMaxTextBoxes(DatabaseHelper dbHelper) {
        TextView txtDaysBetween = (TextView) _view.findViewById(R.id.txtMaxDaysBetween);
        TextView txtMaxFuelUpCost = (TextView) _view.findViewById(R.id.txtMaxFuelUpCost);
        TextView txtMaxGallonsBought = (TextView) _view.findViewById(R.id.txtMaxGallonsBought);
        TextView txtMaxPricePerGallon = (TextView) _view.findViewById(R.id.txtMaxPricePerGallon);
        TextView txtMaxMilesDriven = (TextView) _view.findViewById(R.id.txtMaxMilesDriven);
        TextView txtMaxCostPerMile = (TextView) _view.findViewById(R.id.txtMaxCostPerMile);
        TextView txtMaxMPG = (TextView) _view.findViewById(R.id.txtMaxMPG);

        txtDaysBetween.setText(Collections.max(dbHelper.getAllNumDaysBetween()) + " days");
        txtMaxFuelUpCost.setText("$" + dbHelper.getMaxOfColumn(dbHelper.COLUMN_TOTAL_PRICE, 2));
        txtMaxGallonsBought.setText(dbHelper.getMaxOfColumn(dbHelper.COLUMN_GALLONS_BOUGHT, 3) + "");
        txtMaxPricePerGallon.setText("$" + dbHelper.getMaxOfColumn(dbHelper.COLUMN_PRICE_PER_GALLON, 2));
        txtMaxMilesDriven.setText(Collections.max(dbHelper.getAllBetweenOdometers()) + "");
        txtMaxCostPerMile.setText("$" + Helper.round(Collections.max(dbHelper.getAllCostsPerMilesEntries(false)), 2));
        txtMaxMPG.setText(Helper.round(Collections.max(dbHelper.getAllMilesPerGallonEntries(false)), 2) + " MPG");
    }

    private void populateMinTextBoxes(DatabaseHelper dbHelper) {
        TextView txtDaysBetween = (TextView) _view.findViewById(R.id.txtMinDaysBetween);
        TextView txtMinFuelUpCost = (TextView) _view.findViewById(R.id.txtMinFuelUpCost);
        TextView txtMinGallonsBought = (TextView) _view.findViewById(R.id.txtMinGallonsBought);
        TextView txtMinPricePerGallon = (TextView) _view.findViewById(R.id.txtMinPricePerGallon);
        TextView txtMinMilesDriven = (TextView) _view.findViewById(R.id.txtMinMilesDriven);
        TextView txtMinCostPerMile = (TextView) _view.findViewById(R.id.txtMinCostPerMile);
        TextView txtMinMPG = (TextView) _view.findViewById(R.id.txtMinMPG);

        txtDaysBetween.setText(Collections.min(dbHelper.getAllNumDaysBetween()) + " days");
        txtMinFuelUpCost.setText("$" + dbHelper.getMinOfColumn(dbHelper.COLUMN_TOTAL_PRICE, 2));
        txtMinGallonsBought.setText(dbHelper.getMinOfColumn(dbHelper.COLUMN_GALLONS_BOUGHT, 3) + "");
        txtMinPricePerGallon.setText("$" + dbHelper.getMinOfColumn(dbHelper.COLUMN_PRICE_PER_GALLON, 2));
        txtMinMilesDriven.setText(Collections.min(dbHelper.getAllBetweenOdometers()) + "");
        txtMinCostPerMile.setText("$" + Helper.round(Collections.min(dbHelper.getAllCostsPerMilesEntries(false)), 2));
        txtMinMPG.setText(Helper.round(Collections.min(dbHelper.getAllMilesPerGallonEntries(false)), 2) + " MPG");
    }

    private void populateAverageTextBoxes(DatabaseHelper dbHelper) {
        TextView txtDaysBetween = (TextView) _view.findViewById(R.id.txtAverageDaysBetween);
        TextView txtAverageFuelUpCost = (TextView) _view.findViewById(R.id.txtAverageFuelUpCost);
        TextView txtAverageGallonsBought = (TextView) _view.findViewById(R.id.txtAverageGallonsBought);
        TextView txtAveragePricePerGallon = (TextView) _view.findViewById(R.id.txtAveragePricePerGallon);
        TextView txtAverageMilesDriven = (TextView) _view.findViewById(R.id.txtAverageMilesDriven);
        TextView txtAverageCostPerMile = (TextView) _view.findViewById(R.id.txtAverageCostPerMile);
        TextView txtAverageMPG = (TextView) _view.findViewById(R.id.txtAverageMPG);

        TextView txtMostFrequentGasStation = (TextView) _view.findViewById(R.id.txtMostFrequentGasStation);

        HashMap<String, BigDecimal> hashMap = dbHelper.averagesBetweenFuelUps();
        if(hashMap == null) { return; }

        txtDaysBetween.setText(hashMap.get(Helper.AVERAGE_DAYS_BETWEEN_KEY) + " days");
        txtAverageMilesDriven.setText(hashMap.get(Helper.AVERAGE_MILES_DRIVEN_KEY) + "");
        txtAverageCostPerMile.setText("$" + hashMap.get(Helper.AVERAGE_COST_PER_MILE_KEY));
        txtAverageMPG.setText(hashMap.get(Helper.AVERAGE_MILES_PER_GALLON_KEY) + " MPG");
        txtAverageFuelUpCost.setText("$" + hashMap.get(Helper.AVERAGE_FUELUP_COST_KEY));
        txtAverageGallonsBought.setText(hashMap.get(Helper.AVERAGE_GALLONS_BOUGHT_KEY) + "");
        txtAveragePricePerGallon.setText("$" + hashMap.get(Helper.AVERAGE_PRICE_PER_GALLON_KEY));

        txtMostFrequentGasStation.setText(dbHelper.mostCommonGasStation());
    }
}
