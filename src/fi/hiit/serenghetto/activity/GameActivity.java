package fi.hiit.serenghetto.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapActivity;

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;


public class GameActivity extends MapActivity
{
    private SerenghettoApplication app;
    private MapView mapGame;
    private MapController mapController;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        mapGame = (MapView) findViewById(R.id.mapGame);
        if (mapGame != null) {
            mapGame.setBuiltInZoomControls(true);
            mapController = mapGame.getController();
        }
        else  {
            //[FIXME]
        }

        this.app = (SerenghettoApplication) getApplication();
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

