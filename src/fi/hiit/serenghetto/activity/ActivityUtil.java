package fi.hiit.serenghetto.activity;

import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fi.hiit.serenghetto.R;

public class ActivityUtil
{
    public static boolean onCreateOptionsMenu(Activity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public static boolean onOptionsItemSelected(Activity activity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPrefs:
                activity.startActivity(new Intent(activity, PrefsActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemCodes:
                activity.startActivity(new Intent(activity, CodesActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemGame:
                activity.startActivity(new Intent(activity, GameActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            case R.id.itemScanBarcode:
                activity.startActivity(new Intent(activity, BarcodeScanActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                  .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                  .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                break;
            /*
            case R.id.itemToggleService:
                if (isServiceRunning()) {
                    stopService(new Intent(activity, BarcodesService.class));
                }
                else {
                    startService(new Intent(activity, BarcodesService.class));
                }
                break;
                */
        }
        return true;
    }
}

