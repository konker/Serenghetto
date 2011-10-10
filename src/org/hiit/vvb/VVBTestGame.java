package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;


public class VVBTestGame extends Activity
{
    private static final String TAG = "VVB";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) { 
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemGame:
                startActivity(new Intent(this, VVBTestGame.class));
                break;
            case R.id.itemCodes:
                startActivity(new Intent(this, VVBTestCodes.class));
                break;
            case R.id.itemPrefs:
                startActivity(new Intent(this, VVBTestPreferences.class));
                break;
        }
        return true;
    }
}
