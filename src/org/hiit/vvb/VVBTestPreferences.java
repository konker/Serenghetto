package org.hiit.vvb;

import android.util.Log;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

import android.widget.Toast;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;


public class VVBTestPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    private static final String TAG = "VVBTestPreferences";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        // Setup preferences 
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { 
        Log.d(TAG, "Prefs: " + prefs.getString("username", null) + "/" + prefs.getString("password", null));
        new VVBServerTaskGetToken().execute(prefs.getString("username", null), prefs.getString("password", null));
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
                startActivity(new Intent(this, VVBTestGame.class));
                break;
            case R.id.itemCodes:
                startActivity(new Intent(this, VVBTestCodes.class));
                break;
            case R.id.itemPrefs:
                startActivity(new Intent(this, VVBTestPreferences.class));
                break;
        }
        return true;
    }

    /**
    */
    public class VVBServerTaskGetToken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... code) {
            return new VVBServer("http://vvb.a-z.fi").getToken(code[0], code[1]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(VVBTestPreferences.this, result, Toast.LENGTH_LONG).show();
            return;
        }
    }
}


