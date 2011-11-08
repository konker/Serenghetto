package org.hiit.serenghetto;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.LinearLayout;
import android.location.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapActivity;


public class GameActivity extends MapActivity
{
    private static final String TAG = "SERENGHETTO";

    SerenghettoApplication app;
    LinearLayout linearLayout;
    MapView mapView;
    MapController mapController;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.app = (SerenghettoApplication) getApplication();

        setContentView(R.layout.game);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        mapController = mapView.getController();
        /*[FIXME: what should default zoom level be?]*/
        mapController.setZoom(10);

        // start listening for location
        app.startLocationUpdates();

        // center map to last know location
        Location lastLocation = app.getLastKnownLocation();
        if (lastLocation != null) {
            GeoPoint lastGeoPoint = new GeoPoint((int)(lastLocation.getLatitude()*1E6), (int)(lastLocation.getLongitude()*1E6));
            centerLocation(lastGeoPoint);
        }
        Log.d(TAG, "GameActivity: onCreate");
    }
    
    private void centerLocation(GeoPoint p) {
        mapController.animateTo(p);
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        //app.stopLocationUpdates();
    }
    
    @Override
    protected void onResume() {
        super.onRestart();
        Log.d(TAG, "onResume");
        //app.startLocationUpdates();
    }
    */

    @Override
    protected boolean isRouteDisplayed() {
        return false;
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
}

