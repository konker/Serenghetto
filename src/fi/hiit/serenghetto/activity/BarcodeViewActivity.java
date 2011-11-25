package fi.hiit.serenghetto.activity;

import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.ParseException;
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
import fi.hiit.serenghetto.dto.Barcode;
import fi.hiit.serenghetto.constants.IntentConstants;
import fi.hiit.serenghetto.map.MapCircleOverlay;


public class BarcodeViewActivity extends MapActivity implements OnClickListener
{
    private SerenghettoApplication app;
    private ProgressDialog progress;

    private Button buttonOKBarcode;

    private TextView textBarcodeId;
    private TextView textBarcodeName;
    private TextView textBarcodeCode;
    private TextView textBarcodeTime;
    private TextView textBarcodeTimeReadable;
    private TextView textBarcodeScore;
    private MapView mapBarcodeLocation;
    private TextView textBarcodeLocationLatitude;
    private TextView textBarcodeLocationLongitude;
    private TextView textBarcodeLocationAccuracy;

    private MapController mapController;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodeview);

        this.app = (SerenghettoApplication) getApplication();

        buttonOKBarcode = (Button)findViewById(R.id.buttonOKBarcode);
        
        textBarcodeId = (TextView)findViewById(R.id.textBarcodeId);
        textBarcodeName = (TextView)findViewById(R.id.textBarcodeName);
        textBarcodeCode = (TextView)findViewById(R.id.textBarcodeCode);
        textBarcodeTime = (TextView)findViewById(R.id.textBarcodeTime);
        textBarcodeTimeReadable = (TextView)findViewById(R.id.textBarcodeTimeReadable);
        textBarcodeScore = (TextView)findViewById(R.id.textBarcodeScore);
        mapBarcodeLocation = (MapView)findViewById(R.id.mapBarcodeLocation);
        textBarcodeLocationLatitude = (TextView)findViewById(R.id.textBarcodeLocationLatitude);
        textBarcodeLocationLongitude = (TextView)findViewById(R.id.textBarcodeLocationLongitude);
        textBarcodeLocationAccuracy = (TextView)findViewById(R.id.textBarcodeLocationAccuracy);

        mapController = mapBarcodeLocation.getController();

        // clear fields
        clearFields();

        // set click listeners on the buttons
        buttonOKBarcode.setOnClickListener(this);

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        Log.d(SerenghettoApplication.TAG, "rendering id: " + id);
        renderBarcode(id);

        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onCreate");
    }

    private void renderBarcode(String id) {
        Barcode barcode = this.app.getBarcodeData().getBarcodeById(id);
        if (barcode != null) {
            double latitude = barcode.getLatitude();
            double longitude = barcode.getLongitude();

            textBarcodeId.setText(barcode.getId());
            textBarcodeCode.setText(barcode.getCode());
            textBarcodeName.setText(barcode.getName());
            textBarcodeLocationLatitude.setText(String.valueOf(latitude));
            textBarcodeLocationLongitude.setText(String.valueOf(longitude));
            textBarcodeLocationAccuracy.setText(String.valueOf(barcode.getAccuracy()));

            try {
                Date t = SerenghettoApplication.IN_DATE_FORMAT.parse(barcode.getTimestamp());
                textBarcodeTimeReadable.setText(SerenghettoApplication.OUT_DATE_FORMAT.format(t));
            }
            catch (ParseException ex) {
                textBarcodeTimeReadable.setText(R.string.unknown_timestamp);
            }

            textBarcodeScore.setText(String.valueOf(barcode.getScore()));

            if (barcode.hasLocation()) {
                GeoPoint p = barcode.getGeoPoint();
                mapController.setCenter(p);
                mapBarcodeLocation.getOverlays().add(new MapCircleOverlay(barcode, SerenghettoApplication.MAP_OVERLAY_ALPHA, 0x00, 0x00, 0xFF));
                mapBarcodeLocation.setVisibility(View.VISIBLE);
            }
        }
    }

    private void clearFields() {
        textBarcodeName.setText("");
        textBarcodeCode.setText("");
        textBarcodeTime.setText("");
        textBarcodeTimeReadable.setText("");
        textBarcodeScore.setText("");
        mapBarcodeLocation.setVisibility(View.INVISIBLE);
        textBarcodeLocationLatitude.setText("");
        textBarcodeLocationLongitude.setText("");
        textBarcodeLocationAccuracy.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(SerenghettoApplication.TAG, "BarcodeViewActivity.onDestroy");
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
        if (view == findViewById(R.id.buttonOKBarcode)) {
            this.startActivity(new Intent(this, CodesActivity.class));
            /*
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
              .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            */
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    }
}



