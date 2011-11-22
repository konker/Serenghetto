package fi.hiit.serenghetto.activity;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.location.Location;
import android.database.Cursor;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;
import fi.hiit.serenghetto.constants.IntentConstants;
import fi.hiit.serenghetto.dto.Barcode;
import fi.hiit.serenghetto.map.MapCircleOverlay;


public class GameActivity extends MapActivity
{
    private SerenghettoApplication app;
    private MapView mapGame;
    private MapController mapController;
    private BestLocationEstimateReceiver receiver;
    private IntentFilter filter;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        this.app = (SerenghettoApplication) getApplication();

        mapGame = (MapView) findViewById(R.id.mapGame);
        if (mapGame != null) {
            mapGame.setBuiltInZoomControls(true);
            mapController = mapGame.getController();
            mapController.setZoom(12);
            
            initOverlays();
        }
        else  {
            //[FIXME]
            Log.d(SerenghettoApplication.TAG, "NO MAP REFERENCE");
        }
    
        // Create the barcodes updated receiver
        receiver = new BestLocationEstimateReceiver();
        filter = new IntentFilter(IntentConstants.NEW_BEST_LOCATION_ESTIMATE_INTENT);

        this.app = (SerenghettoApplication) getApplication();
    }

    protected void initOverlays() {
        // get users' barcodes
        Cursor barcodeCursor = app.getBarcodeData().getBarcodesByUser(app.getUserId());

        if (barcodeCursor != null) {
            Log.d(SerenghettoApplication.TAG, "start cursor walk..");
            while (barcodeCursor.moveToNext()) {
                Barcode b = new Barcode(barcodeCursor);
                GeoPoint p = b.getGeoPoint();
                if (p != null) {
                    Log.d(SerenghettoApplication.TAG, "Adding overlay point: " + b.getLongitude() + "," + b.getLatitude() + ":" + b.getName());
                    mapGame.getOverlays().add(new MapCircleOverlay(p, b.getScore()));
                }
            }
            Log.d(SerenghettoApplication.TAG, "end cursor walk..");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SerenghettoApplication.TAG, "GameActivity.onPause");

        // UNregister the receiver
        if (receiver != null) {
            unregisterReceiver(receiver); 
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SerenghettoApplication.TAG, "GameActivity.onResume");

        // Register the receiver
        if (receiver != null && filter != null) {
            registerReceiver(receiver, filter, null, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(SerenghettoApplication.TAG, "GameActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(SerenghettoApplication.TAG, "GameActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SerenghettoApplication.TAG, "GameActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(SerenghettoApplication.TAG, "GameActivity.onDestroy");
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
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
        }
    }
}

