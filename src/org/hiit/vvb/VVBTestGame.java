package org.hiit.vvb;

import android.app.Activity;
import android.os.Bundle;

import android.app.AlertDialog;

public class VVBTestGame extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
    }

    private void alert(CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
