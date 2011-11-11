package fi.hiit.serenghetto;

import android.util.Log;
import java.util.Iterator;
import android.app.Application;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.location.Location;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import fi.hiit.serenghetto.net.SerenghettoServer;
import fi.hiit.serenghetto.net.Response;
import fi.hiit.serenghetto.dto.Barcode;
import fi.hiit.serenghetto.data.BarcodeData;
import fi.hiit.serenghetto.service.SerenghettoService;
import fi.hiit.serenghetto.constants.PrefKeyConstants;

public class SerenghettoApplication extends Application implements OnSharedPreferenceChangeListener
{
    private static final String TAG = "SERENGHETTO";

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
            Log.d(TAG, ex.toString());
        }

        this.bestLocationEstimate = null;

        //[FIXME: START THE SERVICE?]
        startService(new Intent(this, SerenghettoService.class));

        Log.i(TAG, "App.onCreate: " + hasToken());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        /*
        locationManager.removeUpdates(this);
        */
        barcodeData.close();

        //[FIXME: STOP THE SERVICE?]
        stopService(new Intent(this, SerenghettoService.class));

        Log.i(TAG, "App.onTerminate");
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
    public String getUserId() {
        return server.getUserId();
    }
    public void setUserId(String userId) {
        editor.putString(PrefKeyConstants.USER_ID, userId);
        editor.commit();
        server.setUserId(userId);
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

    /*[FIXME: should this be moved into BarcodeData? Somewhere else?]*/
    public int fetchBarcodes() {
        int count = 0;
        Response response = getServer().getBarcodes();

        //Log.d(TAG, "fetchBarcodes: " + response);
        if (response.getHttpCode() != 500) {
            JSONObject body = (JSONObject)response.getBody();
            if (body != null) {
                JSONArray codes = (JSONArray)response.getBody().get("entries");
                for (Iterator iter = codes.iterator(); iter.hasNext();) {
                    JSONObject jb = (JSONObject)iter.next();

                    /*[FIXME: hardcoded field names?]*/
                    Barcode b = new Barcode(String.valueOf(jb.get("id")), String.valueOf(((JSONObject)jb.get("user")).get("id")), (String)jb.get("code"), (String)jb.get("name"));
                    boolean inserted = barcodeData.insertOrIgnoreBarcode(b);
                    if (inserted) {
                        count = count + 1;
                    }
                }
            }
        }
        else {
            Log.i(TAG, "No codes returned");
        }
        return count;
    }

    /* methods required by OnSharedPreferenceChangeListener */
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(PrefKeyConstants.AUTH_TOKEN)) {
            server.setToken(prefs.getString(PrefKeyConstants.AUTH_TOKEN, null));
        }
        return;
    }
}

