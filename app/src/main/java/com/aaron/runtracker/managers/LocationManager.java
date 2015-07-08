package com.aaron.runtracker.managers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.text.TextUtils;

import com.aaron.runtracker.utils.LogUtil;

/**
 * Created by Aaron on 15/7/8.
 */
public class LocationManager {

    public static final String ACTION_LOCATION = "com.aaron.runtracker.ACTION_LOCATION";

    private static final String TAG = LocationManager.class.getSimpleName();

    private static LocationManager mInstance;

    private Context mContext;
    private android.location.LocationManager mLocationManager;

    public synchronized static LocationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationManager(context);
        }

        return mInstance;
    }

    public LocationManager(Context context) {
        mContext = context.getApplicationContext();
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.POWER_LOW);
        String provider = mLocationManager.getBestProvider(criteria, true);

        if (TextUtils.isEmpty(provider)) return;

        LogUtil.d(TAG, "best provider " + provider);

        Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
        if (lastKnownLocation != null) {
            broadcastLocation(lastKnownLocation);
        }

        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(android.location.LocationManager.KEY_LOCATION_CHANGED, location);
        mContext.sendBroadcast(broadcast);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);

        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }
}
