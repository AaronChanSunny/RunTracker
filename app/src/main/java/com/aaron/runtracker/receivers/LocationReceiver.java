package com.aaron.runtracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import com.aaron.runtracker.utils.LogUtil;

/**
 * Created by Aaron on 15/7/8.
 */
public class LocationReceiver extends BroadcastReceiver {

    private static final String TAG = LocationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (location != null) {
            onLocationReceived(location);
            return;
        }
        // If you get here, something else has happened
        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    protected void onLocationReceived(Location location) {
        LogUtil.d(TAG, this + " Got location from " + location.getProvider() + ": " + location.getLatitude() + ", " + location.getLongitude());
    }

    protected void onProviderEnabledChanged(boolean enabled) {
        LogUtil.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }
}
