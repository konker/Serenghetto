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
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.location.Location;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;
import fi.hiit.serenghetto.constants.IntentConstants;
import fi.hiit.serenghetto.net.ServerTask;
import fi.hiit.serenghetto.net.Response;
import fi.hiit.serenghetto.util.PromptDialog;


public class BarcodeScanActivity extends MapActivity implements OnClickListener
{
    private static final String TAG = "SERENGHETTO";

    private SerenghettoApplication app;
    private ProgressDialog progress;

    private Button buttonSaveBarcode;
    private Button buttonCancelBarcode;
    private Button buttonOKBarcode;

    private TextView textBarcodeCode;

    private TextView textBarcodeTime;
    private TextView textBarcodeTimeReadable;

    private EditText inputBarcodeName;
    private TextView textBarcodeName;

    private MapView mapBarcodeLocation;
    private TextView textBarcodeLocationLatitude;
    private TextView textBarcodeLocationLongitude;
    private TextView textBarcodeLocationAccuracy;

    private MapController mapController;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodescan);

        this.app = (SerenghettoApplication) getApplication();

        buttonSaveBarcode = (Button)findViewById(R.id.buttonSaveBarcode);
        buttonCancelBarcode = (Button)findViewById(R.id.buttonCancelBarcode);
        buttonOKBarcode = (Button)findViewById(R.id.buttonOKBarcode);
        
        textBarcodeCode = (TextView)findViewById(R.id.textBarcodeCode);

        textBarcodeTime = (TextView)findViewById(R.id.textBarcodeTime);
        textBarcodeTimeReadable = (TextView)findViewById(R.id.textBarcodeTimeReadable);

        inputBarcodeName = (EditText)findViewById(R.id.inputBarcodeName);
        textBarcodeName = (TextView)findViewById(R.id.textBarcodeName);

        mapBarcodeLocation = (MapView)findViewById(R.id.mapBarcodeLocation);
        textBarcodeLocationLatitude = (TextView)findViewById(R.id.textBarcodeLocationLatitude);
        textBarcodeLocationLongitude = (TextView)findViewById(R.id.textBarcodeLocationLongitude);
        textBarcodeLocationAccuracy = (TextView)findViewById(R.id.textBarcodeLocationAccuracy);

        mapController = mapBarcodeLocation.getController();

        // clear fields
        clearFields();

        // set click listeners on the buttons
        buttonSaveBarcode.setOnClickListener(this);
        buttonCancelBarcode.setOnClickListener(this);
        buttonOKBarcode.setOnClickListener(this);

        AlertDialog ad = IntentIntegrator.initiateScan(this);

        Log.d(TAG, "BarcodeScanActivity.onCreate");
    }

    private void clearFields() {
        buttonSaveBarcode.setVisibility(View.VISIBLE);
        buttonCancelBarcode.setVisibility(View.VISIBLE);
        buttonOKBarcode.setVisibility(View.GONE);

        textBarcodeCode.setText("");
        
        textBarcodeTime.setText("");
        textBarcodeTimeReadable.setText("");

        inputBarcodeName.setText("");
        textBarcodeName.setText("");

        mapBarcodeLocation.setVisibility(View.INVISIBLE);
        textBarcodeLocationLatitude.setText("");
        textBarcodeLocationLongitude.setText("");
        textBarcodeLocationAccuracy.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "BarcodeScanActivity.onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "BarcodeScanActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "BarcodeScanActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "BarcodeScanActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "BarcodeScanActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BarcodeScanActivity.onDestroy");
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return ActivityUtil.onCreateOptionsMenu(this, menu);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	return ActivityUtil.onOptionsItemSelected(this, item);
	}

    public void onClick(View view) {
        if (view == findViewById(R.id.buttonSaveBarcode)) {
            Log.d(TAG, "BarcodeScanActivity.Save");
            progress = ProgressDialog.show(this, "", "Sending...", true);

            String code = textBarcodeCode.getText().toString();
            String name = inputBarcodeName.getText().toString();
            String latitude = textBarcodeLocationLatitude.getText().toString();
            String longitude = textBarcodeLocationLongitude.getText().toString();
            String accuracy = textBarcodeLocationAccuracy.getText().toString();
            String timestamp = textBarcodeTime.getText().toString();

            // send to the server
            new ServerTaskPostCode().execute(code, name, latitude, longitude, accuracy, timestamp);

            textBarcodeName.setText(name);
            inputBarcodeName.setVisibility(View.GONE);
            buttonSaveBarcode.setVisibility(View.GONE);
            buttonCancelBarcode.setVisibility(View.GONE);
        }
        else if (view == findViewById(R.id.buttonCancelBarcode)) {
            Log.d(TAG, "BarcodeScanActivity.Cancel");
            this.startActivity(new Intent(this, GameActivity.class)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
        else if (view == findViewById(R.id.buttonOKBarcode)) {
            this.startActivity(new Intent(this, CodesActivity.class)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        clearFields();

        if (scanResult != null) {
            // populate fields
            textBarcodeCode.setText(scanResult.getContents());
            Location bestLocationEstimate = app.getBestLocationEstimate();

            if (bestLocationEstimate != null) {
                textBarcodeTime.setText(String.format("%d", bestLocationEstimate.getTime()));
                textBarcodeTimeReadable.setText(String.format("READABLE: %d", bestLocationEstimate.getTime()));

                textBarcodeLocationLatitude.setText(String.format("%f", bestLocationEstimate.getLatitude()));
                textBarcodeLocationLongitude.setText(String.format("%f", bestLocationEstimate.getLongitude()));
                textBarcodeLocationAccuracy.setText(String.format("%f", bestLocationEstimate.getAccuracy()));

                GeoPoint p = new GeoPoint((int)(bestLocationEstimate.getLatitude()*1E6), (int)(bestLocationEstimate.getLongitude()*1E6));
                mapController.setCenter(p);
                mapBarcodeLocation.setVisibility(View.VISIBLE);
            }
            else {
                //TODO: hide the location label and map
                //TODO: add a manual timestamp?
                textBarcodeTime.setText(String.format("NOW: %d", 100));
                textBarcodeTimeReadable.setText(String.format("READABLE NOW: %d", 100));
            }
        }
        else {
            //FIXME: what to do?
        }
    }

    /**
    */
    class ServerTaskPostCode extends ServerTask
    {
        @Override
        protected Response doInBackground(String... args) {
            return BarcodeScanActivity.this.app.getServer().postBarcode(args[0], args[1], args[2], args[3], args[4], args[5]);
        }

        @Override
        protected void handleResult() {
            //[TODO: add to local list of code when OK]
            BarcodeScanActivity.this.progress.dismiss();
            Toast.makeText(BarcodeScanActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
            textBarcodeName.setVisibility(View.VISIBLE);
            buttonOKBarcode.setVisibility(View.VISIBLE);
            return;
        }
    }
}


