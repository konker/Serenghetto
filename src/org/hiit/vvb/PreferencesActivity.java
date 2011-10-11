package org.hiit.vvb;

import android.util.Log;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

import android.app.ProgressDialog;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;


public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    public static final String TAG = "VVB";
    public static final String PREFS_NAME = "_PREFS_";

    ProgressDialog progress;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        // Setup preferences 
        //SharedPreferences prefs = getSharedPreferences(PreferencesActivity.PREFS_NAME, 0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "Prefs: " + prefs.getString("email", null) + "/" + prefs.getString("password", null) + "/" + prefs.getString("authToken", "NOO"));
    }
    
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { 
        if (key != "authToken") {
            progress = ProgressDialog.show(PreferencesActivity.this, "", "Authenticating...", true);
            Log.d(TAG, "Prefs: " + prefs.getString("email", null) + "/" + prefs.getString("password", null));
            new VVBServerTaskGetToken().execute(prefs.getString("email", null), prefs.getString("password", null));
        }
        return;    
    } 
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemGame:
                startActivity(new Intent(this, GameActivity.class));
                break;
            case R.id.itemCodes:
                startActivity(new Intent(this, CodesActivity.class));
                break;
            case R.id.itemPrefs:
                startActivity(new Intent(this, PreferencesActivity.class));
                break;
        }
        return true;
    }

    /**
    */
    public class VVBServerTaskGetToken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... code) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PreferencesActivity.this);
            return new VVBServer("http://vvb.a-z.fi", prefs.getString("authToken", null)).getToken(code[0], code[1]);
        }

        protected void onPostExecute(String result) {
            //SharedPreferences prefs = getSharedPreferences(PreferencesActivity.PREFS_NAME, 0);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PreferencesActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("authToken", result);
            editor.commit();

            PreferencesActivity.this.progress.dismiss();
            Toast.makeText(PreferencesActivity.this, prefs.getString("authToken", "NULL"), Toast.LENGTH_LONG).show();
            return;
        }
    }
}


