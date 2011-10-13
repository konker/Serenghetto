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
    protected VVBApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (VVBApplication) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Called every time user clicks on a menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.app.onOptionsItemSelected(item);
    }

    // Called every time menu is opened
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return app.onMenuOpened(featureId, menu);
    }
}

