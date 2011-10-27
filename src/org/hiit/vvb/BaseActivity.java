package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * The base activity with common features shared by CodesActivity and GameActivity 
 */
public class BaseActivity extends Activity
{
    private static final String TAG = "VVB";

    protected VVBApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (VVBApplication) getApplication();

        Log.d(TAG, "BaseActivity: onCreate");
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
    
    @Override
    protected void onResume() {
        super.onRestart();
        Log.d(TAG, "onResume");
        app.startLocationUpdates();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        app.stopLocationUpdates();
    }
}
