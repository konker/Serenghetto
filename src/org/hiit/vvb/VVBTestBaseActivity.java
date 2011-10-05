package org.hiit.vvb;

import android.app.Activity;
import android.app.AlertDialog;

public class VVBTestBaseActivity extends Activity
{
    protected void alert(CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}

