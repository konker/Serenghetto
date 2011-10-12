package org.hiit.vvb;

import android.util.Log;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;

import android.app.ProgressDialog;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

public class PrefsActivity extends PreferenceActivity implements OnClickListener
{
    public static final String TAG = "VVB";
    public static final String AUTH_TOKEN_KEY = "authToken";

    VVBApplication app;
    ProgressDialog progress;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (VVBApplication) getApplication();

        addPreferencesFromResource(R.xml.prefs);
        setContentView(R.layout.prefs);

        Button buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonUpdate)) {
            progress = ProgressDialog.show(PrefsActivity.this, "", "Authenticating...", true);
            new VVBServerTaskGetToken().execute(app.getPrefs().getString("email", null), app.getPrefs().getString("password", null));
        }
    }
    

    /**
    */
    public class VVBServerTaskGetToken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... code) {
            /*[FIXME: hardcoded server base url]*/
            return new VVBServer("http://vvb.a-z.fi", null).getToken(code[0], code[1]);
        }

        protected void onPostExecute(String json) {
            PrefsActivity.this.progress.dismiss();

            JSONObject result = (JSONObject)JSONValue.parse(json);
            String msg = (String)result.get("msg");
            JSONObject body = (JSONObject)result.get("body");
            if (msg.equals("OK")) {
                PrefsActivity.this.app.getPrefsEditor().putString(VVBApplication.PREF_KEY_AUTH_TOKEN, (String)body.get("token"));
                PrefsActivity.this.app.getPrefsEditor().commit();
                Log.d(TAG, "TOKEN:" + PrefsActivity.this.app.getPrefs().getString(VVBApplication.PREF_KEY_AUTH_TOKEN, "NADA"));
            }
            Toast.makeText(PrefsActivity.this, msg, Toast.LENGTH_LONG).show();
            return;
        }
    }
}


