package fi.hiit.serenghetto.activity;

import android.util.Log;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;

import fi.hiit.serenghetto.SerenghettoApplication;


public class MainActivity extends Activity
{
    private SerenghettoApplication app;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.app = (SerenghettoApplication) getApplication();

        /*[FIXME: new activity needs to replace MainActivity in the stack]*/
        if (this.app.hasToken()) {
            Log.d(SerenghettoApplication.TAG, "has token");
            startActivity(new Intent(this, GameActivity.class));
            /*
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              */
        }
        else {
            Log.d(SerenghettoApplication.TAG, "no has token");
            startActivity(new Intent(this, LoggedOutActivity.class));
            /*
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
              */
        }
        Log.d(SerenghettoApplication.TAG, "MainActivity.onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SerenghettoApplication.TAG, "MainActivity.onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SerenghettoApplication.TAG, "MainActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(SerenghettoApplication.TAG, "MainActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(SerenghettoApplication.TAG, "MainActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SerenghettoApplication.TAG, "MainActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(SerenghettoApplication.TAG, "MainActivity.onDestroy");
    }
}
