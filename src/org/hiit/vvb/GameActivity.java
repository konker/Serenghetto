package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
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
    private static final String TAG = "VVB";
    private static final int IE6 = 1000000;

    VVBApplication app;
    LinearLayout linearLayout;
    MapView mapView;
    MapController mapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (VVBApplication) getApplication();

        setContentView(R.layout.game);
        mapView = (MapView) findViewById(R.id.mapview);
        //mapView.setBuiltInZoomControls(true);

        //mapController = mapView.getController();
        //mapController.setZoom(20);

        // start listening for location
        //app.startLocationUpdates();

        // center map to last know location
        /*
        Location lastLocation = app.getLastKnownLocation();
        if (lastLocation != null) {
            GeoPoint lastGeoPoint = new GeoPoint((int)lastLocation.getLatitude()*IE6, (int)lastLocation.getLongitude()*IE6);
            centerLocation(lastGeoPoint);
        }
        */

        Log.d(TAG, "GameActivity: onCreate");
    }
    
    private void centerLocation(GeoPoint p) {
        mapController.animateTo(p);
    }

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
