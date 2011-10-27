package org.hiit.vvb;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;


public class GameActivity extends BaseActivity
{
    private static final String TAG = "VVB";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Log.d(TAG, "GameActivity: onCreate");
    }
}
