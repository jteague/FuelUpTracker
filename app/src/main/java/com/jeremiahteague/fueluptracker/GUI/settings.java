package com.jeremiahteague.fueluptracker.GUI;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jeremiahteague.fueluptracker.DB.DatabaseHelper;
import com.jeremiahteague.fueluptracker.DB.FuelEntry;
import com.jeremiahteague.fueluptracker.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private View _view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static settings newInstance(String param1) {
        settings fragment = new settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button btnDeleteDB = (Button) _view.findViewById(R.id.deleteAllDBEntries);
        btnDeleteDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_btnDeleteDB(view);
            }
        });

        Button btnExportEntries = (Button) _view.findViewById(R.id.exportEntries);
        btnExportEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onClick_btnExportEntries(view);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnImportEntries = (Button) _view.findViewById(R.id.importEntries);
        btnImportEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onClick_btnImportEntries(view);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        return _view;
    }

    public void onClick_btnDeleteDB(View view) {
        final View vView = view; // final for the alert box

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()).
                setTitle("Delete all entries?").
                setMessage("Are you sure you want to delete all entries?").
                setNegativeButton("No", null).
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(vView.getContext());
                        dbHelper.deleteAllFuelEntries();
                        Toast.makeText(getActivity(), "All Entries Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    // Create a file with the SQLite inserts for all entries and send to...
    public void onClick_btnExportEntries(View view) throws UnsupportedEncodingException {
        DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
        List<FuelEntry> allFuelEntries = dbHelper.getAllFuelEntries();

        String allEntriesInsertText = "";
        for(int i=0; i < allFuelEntries.size(); i++) {
            FuelEntry entry = allFuelEntries.get(i);
            String insert = entry.getSqlInsertString();
            allEntriesInsertText = allEntriesInsertText + "\n" + insert;
        }

        // Create a new txt file
        File file = getTempFile(view.getContext(), "entryExportTemp.txt");

        String fileName = URLEncoder.encode(file.getAbsolutePath(), "UTF-8");
        String PATH =  Environment.getExternalStorageDirectory() + "/" + fileName.trim().toString();
        Uri uri = Uri.parse("file://"+PATH);

        Intent sendIntent = new Intent();
        sendIntent.setType("text/plain");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, allEntriesInsertText);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "FuelUp Tracker - Export: " + (new Date()).toString());
        sendIntent.putExtra(Intent.EXTRA_TITLE, "FuelUp Tracker - Export: " + (new Date()).toString());
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sendIntent, "Send to..."));
    }

    public void onClick_btnImportEntries(View view) throws SQLException
    {
        final View vView = view; // final for the alert box

        final EditText input = new EditText(view.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Import SQLite Statements");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();
                DatabaseHelper dbHelper = new DatabaseHelper(vView.getContext());
                dbHelper.runSqlCmd(inputText);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        }
        catch(IOException e) {

        }
        return file;
    }
}