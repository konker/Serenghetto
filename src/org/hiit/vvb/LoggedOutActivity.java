package org.hiit.vvb;

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


public class LoggedOutActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "VVB";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_out);

        Button buttonPrefs = (Button)findViewById(R.id.buttonPrefs);
        buttonPrefs.setOnClickListener(this);

        Log.d(TAG, "LoggedOutActivity: onCreate");
    }

    /*[FIXME: whhen a user logs in and then returns to this activity, should "re-direct" to GameActivity?]*/

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonPrefs)) {
            startActivity(new Intent(this, PrefsActivity.class)
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }
}

