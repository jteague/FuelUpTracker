package com.jeremiahteague.fueluptracker.GUI;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremiahteague.fueluptracker.DB.DatabaseHelper;
import com.jeremiahteague.fueluptracker.DB.FuelEntry;
import com.jeremiahteague.fueluptracker.Helpers.FuelEntryCursorAdapter;
import com.jeremiahteague.fueluptracker.Helpers.MyDate;
import com.jeremiahteague.fueluptracker.MainActivity;
import com.jeremiahteague.fueluptracker.R;


public class ViewData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper _dbHelper;

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
     * @return A new instance of fragment ViewData.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewData newInstance(String param1, String param2) {
        ViewData fragment = new ViewData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewData() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        _dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_view_data, container, false);

        TextView entryLabel = (TextView) _view.findViewById(R.id.entryCount);
        int count = _dbHelper.getFuelEntryCount();
        entryLabel.setText(count + " entries");

        String[] fromArray = new String[] {_dbHelper.COLUMN_LOCATION, _dbHelper.COLUMN_ODOMETER,
                _dbHelper.COLUMN_DATE, _dbHelper.COLUMN_DATE, _dbHelper.COLUMN_DATE};
        int[] toArray = {R.id.listView_location, R.id.listView_odometer, R.id.listView_Mon,
                R.id.listView_DayNum, R.id.listView_year};
        FuelEntryCursorAdapter adapter = new FuelEntryCursorAdapter(_view.getContext(),
                R.layout.entry_list_view, _dbHelper.getAllFuelEntriesCursor(_dbHelper.COLUMN_ODOMETER, true),
                fromArray, toArray, 0);

        final ListView listView = (ListView) _view.findViewById(R.id.listView);
        listView.setClickable(true);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FuelEntry fuelEntry = _dbHelper.cursorToFuelEntry((Cursor) listView.getItemAtPosition(position));
                onClick_listViewItem(fuelEntry);
            }
        });

        listView.setAdapter(adapter);
        _dbHelper.close();

        return _view;
    }

    public void onClick_listViewItem(FuelEntry fuelEntry) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment frag = new ViewData_Entry().newInstance(String.valueOf(fuelEntry.getId()));
        fragmentManager.beginTransaction().
                replace(R.id.container, frag).
                addToBackStack("viewSingleFuelEntryItem").
                commit();
    }

    /*@Override
    public void onResume() {
        try {
            fuelEntryDao.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        fuelEntryDao.close();
        super.onPause();
    }*/

}
