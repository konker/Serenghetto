package fi.hiit.serenghetto.activity;

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

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;


public class LoggedOutActivity extends Activity implements OnClickListener
{
    private SerenghettoApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_out);

        this.app = (SerenghettoApplication) getApplication();

        Button buttonPrefs = (Button)findViewById(R.id.buttonPrefs);
        buttonPrefs.setOnClickListener(this);

        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(SerenghettoApplication.TAG, "LoggedOutActivity.onDestroy");
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
            startActivity(new Intent(this, PrefsActivity.class));
            /*
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
              */
        }
    }
}


