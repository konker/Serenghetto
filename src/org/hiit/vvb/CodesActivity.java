package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.location.Location; 
import android.location.LocationListener; 
import android.location.LocationManager; 

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class CodesActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "VVB";
    
    ProgressDialog progress;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codes);

        // set a click listener on the scan_code button
        Button buttonScanCode = (Button)findViewById(R.id.buttonScanCode);
        buttonScanCode.setOnClickListener(this);

        /*
        //new VVBServerTaskGetCodes().execute();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        lastLocation = null;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CodesActivity.this);
        Log.d(TAG, "PREF:" + prefs.getString("authCode", "NO"));
        */
    }
    
    /*
    @Override
    protected void onResume() {
        super.onRestart();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        if (lastLocation == null) {
            progress = ProgressDialog.show(CodesActivity.this, "", "Getting location...", true);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    */

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonScanCode)) {
            AlertDialog ad = IntentIntegrator.initiateScan(this);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult.getContents() != null) {
            Log.d(TAG, "Format: " + scanResult.getFormatName() + "\nContents: " + scanResult.getContents());

            // get name from the user
            PromptDialog dlg = new PromptDialog(CodesActivity.this, R.string.titlePrompt, R.string.summaryPrompt) {
                @Override
                public boolean onOkClicked(String input) {
                    Log.d(TAG, input);

                    /*[FIXME: is location mandatory? If so enable/disable scan button?]*/
                    progress = ProgressDialog.show(CodesActivity.this, "", "Sending...", true);
                    String lat = "";
                    String lng = "";
                    String accuracy = "";
                    if (app.getLastLocation() != null) {
                        lat = String.format("%f", app.getLastLocation().getLatitude());
                        lng = String.format("%f", app.getLastLocation().getLongitude());
                        accuracy = String.format("%f", app.getLastLocation().getAccuracy());
                    }
                    new VVBServerTaskPostCode().execute(scanResult.getContents(), input, lat, lng, accuracy);

                    return true;
                }
            };
            dlg.show();
        }
    }

    /**
    */
    public class VVBServerTaskPostCode extends VVBServerTask
    {
        @Override
        protected Response doInBackground(String... args) {
            return CodesActivity.this.app.getServer().postCode(args[0], args[1], args[2], args[3], args[4]);
        }

        @Override
        protected void handleResult() {
            /*[TODO: add to local list of code when OK]*/
            CodesActivity.this.progress.dismiss();
            Toast.makeText(CodesActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
    */
    public class VVBServerTaskGetCodes extends VVBServerTask
    {
        @Override
        protected Response doInBackground(String... params) {
            return CodesActivity.this.app.getServer().getCodes();
        }

        @Override
        protected void handleResult() {
            CodesActivity.this.progress.dismiss();
            Toast.makeText(CodesActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }
    
}
