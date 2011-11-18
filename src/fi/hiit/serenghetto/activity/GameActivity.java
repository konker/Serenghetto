package fi.hiit.serenghetto.activity;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.LinearLayout;
import android.location.Location;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapActivity;

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;
import fi.hiit.serenghetto.constants.IntentConstants;


public class GameActivity extends MapActivity
{
    private static final String TAG = "SERENGHETTO";

    SerenghettoApplication app;
    LinearLayout linearLayout;
    MapView mapGameLocation;
    MapController mapController;
    BestLocationEstimateReceiver receiver;
    IntentFilter filter;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        this.app = (SerenghettoApplication) getApplication();

        /*
        MapView mapGameLocation = (MapView)findViewById(R.id.mapGameLocation);
        Log.d(TAG, "GameActivity.onCreate: mapGameLocation: " + mapGameLocation);
        if (mapGameLocation != null) {
            mapGameLocation.setBuiltInZoomControls(true);
            mapController = mapGameLocation.getController();

            //[FIXME: what should default zoom level be?]
            mapController.setZoom(14);
        }
        else  {
            Log.d(TAG, "GameActivity.onCreate: NO MAP VIEW: " + mapGameLocation);
        }

        // start listening for location
        //app.startLocationUpdates();

        // center map to last know location
        //[FIXME]
        //Location lastLocation = app.getLastKnownLocation();
        //onLocation(lastLocation);
    
        // Create the barcodes updated receiver
        receiver = new BestLocationEstimateReceiver();
        filter = new IntentFilter(IntentConstants.NEW_BEST_LOCATION_ESTIMATE_INTENT);

        */
        Log.d(TAG, "GameActivity.onCreate");
    }
    
    private void onLocation(Location location) {
        centerLocation(location);
    }

    private void centerLocation(Location location) {
        if (location != null) {
            if (mapController != null) {
                GeoPoint p = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
                mapController.animateTo(p);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "GameActivity.onPause");
        //app.stopLocationUpdates();

        // UNregister the receiver
        if (receiver != null) {
            unregisterReceiver(receiver); 
        }
    }
    
    @Override
    protected void onResume() {
        super.onRestart();
        Log.d(TAG, "GameActivity.onResume");
        //app.startLocationUpdates();

        // Register the receiver
        if (receiver != null && filter != null) {
            registerReceiver(receiver, filter, null, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "GameActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "GameActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "GameActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "GameActivity.onDestroy");
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return ActivityUtil.onCreateOptionsMenu(this, menu);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	return ActivityUtil.onOptionsItemSelected(this, item);
	}

    // Receiver to wake up when BarcodesService gets new best location estimate
    class BestLocationEstimateReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = GameActivity.this.app.getBestLocationEstimate();
            GameActivity.this.onLocation(location);
            Log.d(TAG, "GameActivity$BestLocationEstimateReceiver.onReceive");
        }
    }
}

