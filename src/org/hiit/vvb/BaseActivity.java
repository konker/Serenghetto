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
public class BaseActivity extends Activity {
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
        switch (item.getItemId()) {
            case R.id.itemPrefs:
                startActivity(new Intent(this, PrefsActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemCodes:
                startActivity(new Intent(this, CodesActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemGame:
                startActivity(new Intent(this, GameActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemToggleService: 
                if (app.isServiceRunning()) { 
                    stopService(new Intent(this, UpdatesService.class)); 
                }
                else { 
                    startService(new Intent(this, UpdatesService.class)); 
                } 
                break; 
        }
        return true;
    }


    // Called every time menu is opened
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        MenuItem toggleItem = menu.findItem(R.id.itemToggleService);
        if (app.isServiceRunning()) {
            toggleItem.setTitle(R.string.titleServiceStop);
            toggleItem.setIcon(android.R.drawable.ic_media_pause);
        }
        else {
            toggleItem.setTitle(R.string.titleServiceStart);
            toggleItem.setIcon(android.R.drawable.ic_media_play);
        }
        return true;
    }
}

