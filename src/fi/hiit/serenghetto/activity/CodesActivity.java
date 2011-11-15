package fi.hiit.serenghetto.activity;

import android.util.Log;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.preference.PreferenceManager;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.location.Location;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;
import fi.hiit.serenghetto.constants.IntentConstants;
import fi.hiit.serenghetto.net.ServerTask;
import fi.hiit.serenghetto.net.Response;
import fi.hiit.serenghetto.util.PromptDialog;


public class CodesActivity extends Activity implements OnClickListener
{
    private static final String TAG = "SERENGHETTO";

    static final String[] FROM = { "code", "name", "score" };
    static final int[] TO = { R.id.textCode, R.id.textName, R.id.textScore };

    SerenghettoApplication app;
    ProgressDialog progress;
    Cursor cursor;
    ListView listView;
    BarcodesReceiver receiver;
    IntentFilter filter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codes);

        this.app = (SerenghettoApplication) getApplication();

        listView = (ListView)findViewById(R.id.codesList);
        setupList();

        // populate barcode list
        this.setupList();
    
        // Create the barcodes updated receiver
        receiver = new BarcodesReceiver();
        filter = new IntentFilter(IntentConstants.NEW_BARCODES_INTENT);

        Log.d(TAG, "CodesActivity.onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "CodesActivity.onPause");

        // UNregister the receiver
        unregisterReceiver(receiver); 
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "CodesActivity.onResume");

        // Register the receiver
        registerReceiver(receiver, filter, null, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "CodesActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "CodesActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "CodesActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "CodesActivity.onDestroy");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return ActivityUtil.onCreateOptionsMenu(this, menu);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	return ActivityUtil.onOptionsItemSelected(this, item);
	}

    private void setupList() {
        Log.d(TAG, "setupList.PRE:" + this.app.getUserId());
        Cursor cursor = this.app.getBarcodeData().getBarcodesByUser(this.app.getUserId());
        Log.d(TAG, "setupList.POST");
        /*
        Log.d(TAG, "start cursor walk..");
        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Log.i(TAG, code + "->" + name);
        }
        Log.d(TAG, "end cursor walk..");
        */

        if (cursor == null) {
            /*[TODO: "no barcodes found" message]*/
        }
        else {
            startManagingCursor(cursor);
            Log.d(TAG, "cursor received: " + cursor + ": " + cursor.isClosed());

            // Setup Adapter
            ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.barcoderow, cursor, FROM, TO);
            listView.setAdapter(adapter);
            Log.d(TAG, "cursor received': " + cursor + ": " + cursor.isClosed());
        }
    }

    public void onClick(View view) {
        //TODO: handle click for list items
    }


    // Receiver to wake up when SerenghettoService gets new barcodes
    class BarcodesReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            CodesActivity.this.setupList();
            Log.d(TAG, "BarcodesReceiver: onReceived");
        }
    }
}

