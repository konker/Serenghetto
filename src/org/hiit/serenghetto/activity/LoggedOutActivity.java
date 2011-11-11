package org.hiit.serenghetto.activity;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;

import org.hiit.serenghetto.R;
import org.hiit.serenghetto.SerenghettoApplication;


public class LoggedOutActivity extends Activity implements OnClickListener
{
    private static final String TAG = "SERENGHETTO";

    SerenghettoApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_out);

        this.app = (SerenghettoApplication) getApplication();

        Button buttonPrefs = (Button)findViewById(R.id.buttonPrefs);
        buttonPrefs.setOnClickListener(this);

        Log.d(TAG, "LoggedOutActivity.onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "LoggedOutActivity.onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "LoggedOutActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "LoggedOutActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "LoggedOutActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "LoggedOutActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "LoggedOutActivity.onDestroy");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return ActivityUtil.onCreateOptionsMenu(this, menu);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	return ActivityUtil.onOptionsItemSelected(this, item);
	}

    /*[FIXME: when a user logs in and then returns to this activity, should "re-direct" to GameActivity?]*/

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonPrefs)) {
            startActivity(new Intent(this, PrefsActivity.class)
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }
}


