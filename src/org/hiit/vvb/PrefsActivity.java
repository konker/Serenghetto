package org.hiit.vvb;

import java.util.Map;
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

        Log.d(TAG, "PrefsActivity: onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.app.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return app.onMenuOpened(featureId, menu);
    }

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonUpdate)) {
            progress = ProgressDialog.show(PrefsActivity.this, "", "Authenticating...", true);
            new VVBServerTaskAuthorize().execute(app.getPrefs().getString("email", null), app.getPrefs().getString("password", null));
        }
    }
    

    /**
    */
    public class VVBServerTaskAuthorize extends VVBServerTask
    {
        @Override
        protected Response doInBackground(String... code) {
            return PrefsActivity.this.app.getServer().authorzie(code[0], code[1]);
        }

        @Override
        protected void handleResult() {
            PrefsActivity.this.progress.dismiss();
            if (response.getHttpCode() == 201) {
                Map body = (Map)response.getBody();
                Map user = (Map)body.get("user");
                PrefsActivity.this.app.setToken((String)user.get("token"));
                PrefsActivity.this.app.setUserId(String.valueOf(user.get("id")));
            }
            else {
                Log.d(TAG, "http code: " + response.getHttpCode());
            }
            /*[FIXME: handle != 201 error?]*/
            Toast.makeText(PrefsActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }
}

