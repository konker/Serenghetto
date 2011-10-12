package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import android.widget.Toast;


public class MainActivity extends BaseActivity
{
    private static final String TAG = "VVB";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*[FIXME: new activity needs to replace MainActivity in the stack]*/
        if (app.hasToken()) {
            Log.d(TAG, "has token");
            startActivity(new Intent(this, GameActivity.class)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else {
            Log.d(TAG, "no has token");
            startActivity(new Intent(this, LoggedOutActivity.class)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}

