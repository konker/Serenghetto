package fi.hiit.serenghetto.activity;

import android.util.Log;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.ListActivity;
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
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
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


public class CodesActivity extends ListActivity implements OnItemClickListener, OnItemLongClickListener
{
    static final String[] FROM = { "_id", "code", "name", "score" };
    static final int[] TO = { R.id.textBarcodeId, R.id.textBarcodeCode, R.id.textBarcodeName, R.id.textBarcodeScore };

    private SerenghettoApplication app;
    private ProgressDialog progress;
    private Cursor cursor;
    private BarcodesReceiver receiver;
    private IntentFilter filter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codes);

        this.app = (SerenghettoApplication) getApplication();

        //listView = (ListView)findViewById(R.id.codesList);
        //setupList();
    
        // Create the barcodes updated receiver
        receiver = new BarcodesReceiver();
        filter = new IntentFilter(IntentConstants.NEW_BARCODES_INTENT);

        Log.d(SerenghettoApplication.TAG, "CodesActivity.onCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SerenghettoApplication.TAG, "CodesActivity.onPause");

        if (cursor != null) {
            cursor.close();
        }

        // UNregister the receiver
        unregisterReceiver(receiver); 
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SerenghettoApplication.TAG, "CodesActivity.onResume");

        // populate barcode list
        this.setupList();

        // Register the receiver
        registerReceiver(receiver, filter, null, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(SerenghettoApplication.TAG, "CodesActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(SerenghettoApplication.TAG, "CodesActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (cursor != null) {
            cursor.close();
        }

        Log.d(SerenghettoApplication.TAG, "CodesActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cursor != null) {
            cursor.close();
        }

        Log.d(SerenghettoApplication.TAG, "CodesActivity.onDestroy");
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
        cursor = this.app.getBarcodeData().getBarcodesByUser(this.app.getUserId());
        /*
        Log.d(SerenghettoApplication.TAG, "start cursor walk..");
        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String score = cursor.getString(cursor.getColumnIndex("score"));
            Log.i(SerenghettoApplication.TAG, code + "->" + name + ", " + score);
        }
        Log.d(SerenghettoApplication.TAG, "end cursor walk..");
        */
        if (cursor == null) {
            /*[TODO: "no barcodes found" message]*/
        }
        else {
            startManagingCursor(cursor);

            // Setup Adapter
            ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.barcoderow, cursor, FROM, TO);
            ListView listView = getListView();
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
            listView.setAdapter(adapter);

            //cursor.close();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        //TODO: handle click for list items
        TextView textBarcodeId = (TextView)view.findViewById(R.id.textBarcodeId);
        Log.d(SerenghettoApplication.TAG, "CLICK: " + textBarcodeId.getText());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        //TODO: handle long click for list items
        TextView textBarcodeId = (TextView)view.findViewById(R.id.textBarcodeId);
        Log.d(SerenghettoApplication.TAG, "LONG CLICK: " + textBarcodeId.getText());

        Intent i = new Intent(this, BarcodeViewActivity.class);
        i.putExtra("id", textBarcodeId.getText());

        startActivity(i);
          /*
          .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
          .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
          */

        return true;
    }


    // Receiver to wake up when SerenghettoService gets new barcodes
    class BarcodesReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            CodesActivity.this.setupList();
            Log.d(SerenghettoApplication.TAG, "BarcodesReceiver: onReceived");
        }
    }
}

