/**
*/

package org.hiit.vvb;

import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class VVBApplication extends Application implements OnSharedPreferenceChangeListener, LocationListener
{
    private static final String TAG = "VVB";
    private static final int TIME_DELTA_SIGNIFICANT_WINDOW_MS = 1000 * 60 * 2; // 2 minutes

    private static final String SERVER_BASE_URL = "http://serenghetto.herokuapp.com";

    public static final String PREF_KEY_AUTH_TOKEN = "authToken";
    public static final String PREF_KEY_USER_ID = "userId";
    public static final String PREF_KEY_EMAIL = "email";
    public static final String PREF_KEY_EMAIL_DIRTY = "emailDirty";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_PASSWORD_DIRTY = "passwordDirty";

    private BarcodeData barcodeData;
    private VVBServer server;
    private boolean serviceRunning;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private LocationManager locationManager;
    private Location bestLocationEstimate;

    @Override
    public void onCreate() {
        super.onCreate();

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = prefs.edit();
        this.prefs.registerOnSharedPreferenceChangeListener(this);

        String token = prefs.getString(PREF_KEY_AUTH_TOKEN, null);
        String userId = prefs.getString(PREF_KEY_USER_ID, null);
        this.server = new VVBServer(SERVER_BASE_URL, token, userId);

        try {
            this.barcodeData = new BarcodeData(this);
        }
        catch (Exception ex) {
            /*[FIXME]*/
            /* now what? */
        }

        this.bestLocationEstimate = null;

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //[FIXME: START THE SERVICE HERE?]

        Log.i(TAG, "App.onCreated: " + hasToken());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        locationManager.removeUpdates(this);
        Log.i(TAG, "onTerminated");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPrefs:
                startActivity(new Intent(this, PrefsActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemCodes:
                startActivity(new Intent(this, CodesActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemGame:
                startActivity(new Intent(this, GameActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemToggleService:
                if (isServiceRunning()) {
                    stopService(new Intent(this, UpdatesService.class));
                }
                else {
                    startService(new Intent(this, UpdatesService.class));
                }
                break;
        }
        return true;
    }
    public boolean onMenuOpened(int featureId, Menu menu) {
        MenuItem toggleItem = menu.findItem(R.id.itemToggleService);
        if (isServiceRunning()) {
            toggleItem.setTitle(R.string.titleServiceStop);
            toggleItem.setIcon(android.R.drawable.ic_media_pause);
        }
        else {
            toggleItem.setTitle(R.string.titleServiceStart);
            toggleItem.setIcon(android.R.drawable.ic_media_play);
        }
        return true;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
    public SharedPreferences.Editor getPrefsEditor() {
        return editor;
    }

    public boolean hasToken() {
        return (server.getToken() != null);
    }
    public String getuserId() {
        return server.getUserId();
    }
    public void setUserId(String userId) {
        editor.putString(PREF_KEY_USER_ID, userId);
        editor.commit();
        server.setUserId(userId);
    }

    public String getToken() {
        return server.getToken();
    }
    public void setToken(String token) {
        editor.putString(PREF_KEY_AUTH_TOKEN, token);
        editor.commit();
        server.setToken(token);
    }

    public VVBServer getServer() {
        return server;
    }

    public Location getBestLocationEstimate() {
        return bestLocationEstimate;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }
    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

    public BarcodeData getBarcodeData() {
        return barcodeData;
    }

    /* methods required by OnSharedPreferenceChangeListener */
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(PREF_KEY_AUTH_TOKEN)) {
            server.setToken(prefs.getString(PREF_KEY_AUTH_TOKEN, null));
        }
        return;
    }

    public void startLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }
    // Methods required by LocationListener
    public void onLocationChanged(Location location) {
        Log.d(TAG, "LOC: " + location.toString());
        if (isBetterLocation(location)) {
            Log.d(TAG, "LOC better: " + location.toString());
            bestLocationEstimate = location;
        }
    }
    public void onProviderDisabled(String provider) {
    }
    public void onProviderEnabled(String provider) {
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public boolean isBetterLocation(Location location) {
        if (bestLocationEstimate == null) {
            return true;
        }

        // check whether the new location fix is newer
        long timeDelta = location.getTime() - bestLocationEstimate.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_DELTA_SIGNIFICANT_WINDOW_MS;
        boolean isSignificantlyOlder = timeDelta < -TIME_DELTA_SIGNIFICANT_WINDOW_MS;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        }
        else if (isSignificantlyOlder) {
            return false;
        }

        // check accuracy
        int accuracyDelta = (int)(location.getAccuracy() - bestLocationEstimate.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(), bestLocationEstimate.getProvider());

        if (isMoreAccurate) {
            return true;
        }
        else if (isNewer && !isLessAccurate) {
            return true;
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }
    private boolean isSameProvider(String p1, String p2) {
        if (p1 == null) {
            return (p2 == null);
        }
        return p1.equals(p2);
    }
}


