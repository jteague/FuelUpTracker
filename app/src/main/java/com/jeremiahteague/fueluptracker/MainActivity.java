package com.jeremiahteague.fueluptracker;

import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.jeremiahteague.fueluptracker.GUI.Averages;
import com.jeremiahteague.fueluptracker.GUI.NavigationDrawerFragment;
import com.jeremiahteague.fueluptracker.GUI.NewEntry;
import com.jeremiahteague.fueluptracker.GUI.ViewData;
import com.jeremiahteague.fueluptracker.GUI.settings;
import com.jeremiahteague.fueluptracker.Helpers.Helper;
import com.jeremiahteague.fueluptracker.Helpers.MyLocationListener;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment _navigationDrawerFragment;
    private LocationManager _locationManager;
    private LocationListener _locationListener;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        _navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set up the GPS listener
        _locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        _locationListener = new MyLocationListener();
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 20, _locationListener);

        // Set the initial view/fragment
        switchFragmentView(FragmentName.FRAGMENT_VIEW_DATA);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Remove the keyboard (if present)
        Helper.hideKeyboard(this);

        switchFragmentView(position);
    }

    private void switchFragmentView(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment frag;

        switch(position)
        {
            case FragmentName.FRAGMENT_AVERAGES:
                frag = new Averages().newInstance("averages", "case2");
                fragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("viewAverages").commit();
                break;
            case FragmentName.FRAGMENT_NEW_ENTRY:
                frag = new NewEntry().newInstance("new entry", "case3");
                fragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("addNewEntry").commit();
                break;
            case FragmentName.FRAGMENT_SETTINGS:
                frag = new settings().newInstance("settings");
                fragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("viewSettings").commit();
                break;
            case FragmentName.FRAGMENT_VIEW_DATA:
            default:
                frag = new ViewData().newInstance("view data", "case1");
                fragmentManager.beginTransaction().replace(R.id.container, frag).addToBackStack("viewData").commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.navBar_ViewDataHeader);
                break;
            case 2:
                mTitle = getString(R.string.navBar_ViewAveragesHeader);
                break;
            case 3:
                mTitle = getString(R.string.navBar_NewEntryHeader);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!_navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        _locationManager.removeUpdates(_locationListener);
        super.onPause();
    }

    @Override
    public void onStop() {
        _locationManager.removeUpdates(_locationListener);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        _locationManager.removeUpdates(_locationListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 20, _locationListener);
        super.onResume();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class FragmentName {
        public static final int FRAGMENT_VIEW_DATA =    0;
        public static final int FRAGMENT_AVERAGES =     1;
        public static final int FRAGMENT_NEW_ENTRY =    2;
        public static final int FRAGMENT_SETTINGS =     3;
    }

}
