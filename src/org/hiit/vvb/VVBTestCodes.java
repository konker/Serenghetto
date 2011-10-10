package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

import android.app.AlertDialog;
import android.os.AsyncTask;

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.location.Location; 
import android.location.LocationListener; 
import android.location.LocationManager; 

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class VVBTestCodes extends Activity implements OnClickListener, LocationListener
{
    private static final String TAG = "VVB";
    
    LocationManager locationManager;
    Location lastLocation;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codes);

        // set a click listener on the scan_code button
        Button scan_code = (Button)findViewById(R.id.buttonScanCode);
        scan_code.setOnClickListener(this);

        //new VVBServerTaskGetCodes().execute();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        lastLocation = null;
    }
    
    @Override
    protected void onResume() {
        super.onRestart();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
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

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonScanCode)) {
            AlertDialog ad = IntentIntegrator.initiateScan(this);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.d(TAG, "Format: " + scanResult.getFormatName() + "\nContents: " + scanResult.getContents());
            if (lastLocation != null) {
                Log.d(TAG, String.format("%f", lastLocation.getLatitude()) + ", " + String.format("%f", lastLocation.getLongitude()) + ", " + String.format("%f", lastLocation.getAccuracy()));
                new VVBServerTaskPostCode().execute(scanResult.getContents(), String.format("%f", lastLocation.getLatitude()), String.format("%f", lastLocation.getLongitude()), String.format("%f", lastLocation.getAccuracy()));
            }
            else {
                Log.d(TAG, "NO LOCATION");
            }
        }
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

    /**
    */
    public class VVBServerTaskPostCode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... code) {
            return new VVBServer("http://vvb.a-z.fi").postCode(code[0], code[1], code[2], code[3]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(VVBTestCodes.this, result, Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
    */
    public class VVBServerTaskGetCodes extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return new VVBServer("http://vvb.a-z.fi").getCodes();
        }

        protected void onPostExecute(String result) {
            Toast.makeText(VVBTestCodes.this, result, Toast.LENGTH_LONG).show();
            return;
        }
    }
    
}

