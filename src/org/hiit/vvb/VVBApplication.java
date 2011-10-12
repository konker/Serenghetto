/**
*/

package org.hiit.vvb;

import android.util.Log;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;



public class VVBApplication extends Application implements OnSharedPreferenceChangeListener {
    private static final String TAG = "VVB";

    public static final String PREF_KEY_AUTH_TOKEN = "authToken";
    public static final String PREF_KEY_EMAIL = "email";
    public static final String PREF_KEY_EMAIL_DIRTY = "emailDirty";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_PASSWORD_DIRTY = "passwordDirty";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private LocationManager locationManager;
    private Location lastLocation;
    private String token;

    @Override
    public void onCreate() {
        super.onCreate();

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = prefs.edit();
        this.prefs.registerOnSharedPreferenceChangeListener(this);

        this.token = prefs.getString(PREF_KEY_AUTH_TOKEN, null);

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.lastLocation = null;

        Log.i(TAG, "onCreated: " + hasToken());
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
    public SharedPreferences.Editor getPrefsEditor() {
        return editor;
    }

    public boolean hasToken() {
        return (token != null);
    }
    public String getToken() {
        return token;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminated");
    }

    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(PREF_KEY_AUTH_TOKEN)) {
            token = prefs.getString(PREF_KEY_AUTH_TOKEN, null);
        }
        return;
    }
}


