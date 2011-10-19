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

    private static final String SERVER_BASE_URL = "http://zebraz.herokuapp.com";

    public static final String PREF_KEY_AUTH_TOKEN = "authToken";
    public static final String PREF_KEY_EMAIL = "email";
    public static final String PREF_KEY_EMAIL_DIRTY = "emailDirty";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_PASSWORD_DIRTY = "passwordDirty";

    private VVBServer server;
    private boolean serviceRunning;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private LocationManager locationManager;
    private Location lastLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = prefs.edit();
        this.prefs.registerOnSharedPreferenceChangeListener(this);

        String token = prefs.getString(PREF_KEY_AUTH_TOKEN, null);
        this.server = new VVBServer(SERVER_BASE_URL, token);

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.lastLocation = null;

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        /*
        if (lastLocation == null) {
            progress = ProgressDialog.show(CodesActivity.this, "", "Getting location...", true);
        }
        */
        //START THE SERVICE HERE?

        Log.i(TAG, "onCreated: " + hasToken());
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

    public Location getLastLocation() {
        return lastLocation;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }
    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

    /* methods required by OnSharedPreferenceChangeListener */
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(PREF_KEY_AUTH_TOKEN)) {
            server.setToken(prefs.getString(PREF_KEY_AUTH_TOKEN, null));
        }
        return;
    }

    // Methods required by LocationListener 
    public void onLocationChanged(Location location) {
        Log.d(TAG, location.toString());
        lastLocation = location;
    }
    public void onProviderDisabled(String provider) {
    }
    public void onProviderEnabled(String provider) {
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}


