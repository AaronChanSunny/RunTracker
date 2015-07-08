package com.aaron.runtracker.fragments;

import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.runtracker.R;
import com.aaron.runtracker.managers.LocationManager;
import com.aaron.runtracker.receivers.LocationReceiver;

/**
 * Created by Aaron on 15/7/8.
 */
public class RunFragment extends Fragment {

    private Toolbar mToolbar;
    private Button mStartButton, mStopButton;
    private TextView mLatitudeTextView, mLongitudeTextView, mAltitudeTextView;
    private LocationManager mLocationManager;
    private LocationReceiver mLocationReceiver;
    private Location mLastKnownLocation;

    public static Fragment newInstance() {
        return new RunFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = LocationManager.getInstance(getActivity());
        mLocationReceiver = new LocationReceiver() {
            @Override
            protected void onLocationReceived(Location location) {
                super.onLocationReceived(location);
                mLastKnownLocation = location;

                updateUI();
            }

            @Override
            protected void onProviderEnabledChanged(boolean enabled) {
                super.onProviderEnabledChanged(enabled);

                int toastTextResId = enabled ? R.string.gps_enabled : R.string.gps_disabled;
                Toast.makeText(getActivity(), toastTextResId, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void updateUI() {
        if (mLastKnownLocation != null) {
            mLatitudeTextView.setText(mLastKnownLocation.getLatitude() + "");
            mLongitudeTextView.setText(mLastKnownLocation.getLongitude() + "");
            mAltitudeTextView.setText(mLastKnownLocation.getAltitude() + "");
        }

        boolean isStarted = mLocationManager.isTrackingRun();
        mStartButton.setEnabled(!isStarted);
        mStopButton.setEnabled(isStarted);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        mStartButton = (Button) view.findViewById(R.id.btn_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationManager.startLocationUpdates();

                updateUI();
            }
        });

        mStopButton = (Button) view.findViewById(R.id.btn_stop);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationManager.stopLocationUpdates();

                updateUI();
            }
        });

        mLatitudeTextView = (TextView) view.findViewById(R.id.tv_latitude);
        mLongitudeTextView = (TextView) view.findViewById(R.id.tv_longitude);
        mAltitudeTextView = (TextView) view.findViewById(R.id.tv_altitude);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(LocationManager.ACTION_LOCATION);
        getActivity().registerReceiver(mLocationReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mLocationReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
