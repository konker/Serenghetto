package org.hiit.serenghetto;

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
    private static final String TAG = "SERENGHETTO";

    protected SerenghettoApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (SerenghettoApplication) getApplication();

        Log.d(TAG, "BaseActivity.onCreate");
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
        super.onResume();
        Log.d(TAG, "BaseActivity.onResume");
        //app.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "BaseActivity.onPause");
        //app.stopLocationUpdates();
    }
}

