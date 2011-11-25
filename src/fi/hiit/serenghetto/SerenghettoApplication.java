package fi.hiit.serenghetto;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.location.Location;

import fi.hiit.serenghetto.remote.SerenghettoServer;
import fi.hiit.serenghetto.remote.Response;
import fi.hiit.serenghetto.dto.Barcode;
import fi.hiit.serenghetto.data.BarcodeData;
import fi.hiit.serenghetto.service.SerenghettoService;
import fi.hiit.serenghetto.constants.PrefKeyConstants;

public class SerenghettoApplication extends Application implements OnSharedPreferenceChangeListener
{
    public static final String TAG = "SERENGHETTO";
    public static final String ERROR_TAG = "SERENGHETTO:ERROR";
    public static final int MAP_OVERLAY_ALPHA = 0x20;
    public static SimpleDateFormat OUT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static SimpleDateFormat IN_DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'");


    private static final String SERVER_BASE_URL = "http://serenghetto.herokuapp.com";

    private Location bestLocationEstimate;
    private BarcodeData barcodeData;
    private SerenghettoServer server;
    private boolean serviceRunning;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        IN_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = prefs.edit();
        this.prefs.registerOnSharedPreferenceChangeListener(this);

        String token = prefs.getString(PrefKeyConstants.AUTH_TOKEN, null);
        String userId = prefs.getString(PrefKeyConstants.USER_ID, null);

        this.server = new SerenghettoServer(SERVER_BASE_URL, token, userId);

        try {
            this.barcodeData = new BarcodeData(this);
        }
        catch (Exception ex) {
            /*[FIXME]*/
            /* now what? */
            Log.d(SerenghettoApplication.TAG, "Could not instantiate barcodeData: " + ex.toString());
        }

        this.bestLocationEstimate = null;

        //[FIXME: START THE SERVICE?]
        startService(new Intent(this, SerenghettoService.class));

        Log.d(SerenghettoApplication.TAG, "App.onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        barcodeData.close();

        //[FIXME: STOP THE SERVICE?]
        stopService(new Intent(this, SerenghettoService.class));

        Log.i(SerenghettoApplication.TAG, "App.onTerminate");
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
    public SharedPreferences.Editor getPrefsEditor() {
        return editor;
    }

    public String getUserId() {
        return server.getUserId();
    }
    public void setUserId(String userId) {
        editor.putString(PrefKeyConstants.USER_ID, userId);
        editor.commit();
        server.setUserId(userId);
    }

    public boolean hasToken() {
        return (server.getToken() != null);
    }
    public String getToken() {
        return server.getToken();
    }
    public void setToken(String token) {
        editor.putString(PrefKeyConstants.AUTH_TOKEN, token);
        editor.commit();
        server.setToken(token);
    }

    public SerenghettoServer getServer() {
        return server;
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

    public Location getBestLocationEstimate() {
        return bestLocationEstimate;
    }
    public void setBestLocationEstimate(Location bestLocationEstimate) {
        Log.d(SerenghettoApplication.TAG, "SerenghettoApplication.setBestLocationEstimate");
        this.bestLocationEstimate = bestLocationEstimate;
    }

    /* methods required by OnSharedPreferenceChangeListener */
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(PrefKeyConstants.AUTH_TOKEN)) {
            server.setToken(prefs.getString(PrefKeyConstants.AUTH_TOKEN, null));
        }
        return;
    }
}

