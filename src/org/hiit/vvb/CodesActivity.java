package org.hiit.vvb;

import android.util.Log;
import java.util.List;
import java.util.Map;
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

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;

import android.location.Location; 


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class CodesActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "VVB";

    static final String[] FROM = { "code", "name" };
    static final int[] TO = { R.id.textCode, R.id.textName };
    
    ProgressDialog progress;
    Cursor cursor; 
    ListView listView; 
    //SimpleCursorAdapter adapter; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codes);

        listView = (ListView)findViewById(R.id.codesList);
        setupList();

        // set a click listener on the scan_code button
        Button buttonScanCode = (Button)findViewById(R.id.buttonScanCode);
        buttonScanCode.setOnClickListener(this);

        // start listening for location
        app.startLocationUpdates();

        // fetch the user's codes
        //new VVBServerTaskGetCodes().execute();
        Log.d(TAG, "CodesActivity: onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setupList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupList() {
        Cursor cursor = this.app.getBarcodeData().getBarcodesByUser(this.app.getUserId());
        if (cursor == null) {
            /*[TODO: "no barcodes found" message]*/
        }
        else {
            startManagingCursor(cursor);
            Log.d(TAG, "cursor received: " + cursor + ": " + cursor.isClosed());

            // Setup Adapter
            ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.barcoderow, cursor, FROM, TO);
            //adapter.setViewBinder(VIEW_BINDER);
            listView.setAdapter(adapter);
            Log.d(TAG, "cursor received': " + cursor + ": " + cursor.isClosed());
        }
    }

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonScanCode)) {
            AlertDialog ad = IntentIntegrator.initiateScan(this);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult.getContents() != null) {
            Log.d(TAG, "Format: " + scanResult.getFormatName() + "\nContents: " + scanResult.getContents());

            // get name from the user
            PromptDialog dlg = new PromptDialog(CodesActivity.this, R.string.titlePrompt, R.string.summaryPrompt) {
                @Override
                public boolean onOkClicked(String input) {
                    Log.d(TAG, input);

                    /*[FIXME: is location mandatory? If so enable/disable scan button?]*/
                    progress = ProgressDialog.show(CodesActivity.this, "", "Sending...", true);
                    Location bestLocationEstimate = app.getBestLocationEstimate();
                    String lat = "";
                    String lng = "";
                    String accuracy = "";
                    String timestamp = "";
                    if (bestLocationEstimate != null) {
                        lat = String.format("%f", bestLocationEstimate.getLatitude());
                        lng = String.format("%f", bestLocationEstimate.getLongitude());
                        accuracy = String.format("%f", bestLocationEstimate.getAccuracy());
                        timestamp = String.format("%d", bestLocationEstimate.getTime());
                    }

                    // send to the server
                    new VVBServerTaskPostCode().execute(scanResult.getContents(), input, lat, lng, accuracy, timestamp);

                    // store in local db?


                    return true;
                }
            };
            dlg.show();
        }
    }

    /**
    */
    public class VVBServerTaskPostCode extends VVBServerTask
    {
        @Override
        protected Response doInBackground(String... args) {
            return CodesActivity.this.app.getServer().postCode(args[0], args[1], args[2], args[3], args[4], args[5]);
        }

        @Override
        protected void handleResult() {
            /*[TODO: add to local list of code when OK]*/
            CodesActivity.this.progress.dismiss();
            Toast.makeText(CodesActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
    */
    /*
    public class VVBServerTaskGetCodes extends VVBServerTask
    {
        @Override
        protected Response doInBackground(String... params) {
            return CodesActivity.this.app.getServer().getCodes();
        }

        @Override
        protected void handleResult() {
            //CodesActivity.this.progress.dismiss();
            //Toast.makeText(CodesActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            List entries = (List)response.getBody().get("entries");
            for (Object b : entries) {
                
                Log.d(TAG, ((Map)b).get("code").toString());
            }
            return;
        }
    }
    */
    
}

