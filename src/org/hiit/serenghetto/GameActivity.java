package org.hiit.serenghetto;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapActivity;


public class GameActivity extends MapActivity
{
    private static final String TAG = "SERENGHETTO";

    protected SerenghettoApplication app;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.app = (SerenghettoApplication) getApplication();

        setContentView(R.layout.game);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        Log.d(TAG, "MAPVIEW: " + mapView);
        mapView.setBuiltInZoomControls(true);
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

