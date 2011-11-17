package fi.hiit.serenghetto.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import fi.hiit.serenghetto.R;
import fi.hiit.serenghetto.SerenghettoApplication;
import fi.hiit.serenghetto.constants.IntentConstants;
import fi.hiit.serenghetto.dto.Barcode;

public class SerenghettoService extends Service {
    private static final String TAG = "SERENGHETTO";

    static final int DELAY_MS = 30000;
    private boolean runFlag = false;
    private Updater updater;
    private SerenghettoApplication app;
    private LocationHelper locationHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.app = (SerenghettoApplication) getApplication();
        this.updater = new Updater();
        this.locationHelper = new LocationHelper();

        Log.d(TAG, "SerenghettoService.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        this.runFlag = true;
        this.updater.start();
        this.app.setServiceRunning(true);

        //[FIXME: START LISTENING FOR LOCATION?]
        this.locationHelper.startLocationUpdates();

        Log.d(TAG, "SerenghettoService.onStart");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runFlag = false;
        this.updater.interrupt();
        this.updater = null;
        this.app.setServiceRunning(false);

        //[FIXME: STOP LISTENING FOR LOCATION?]
        this.locationHelper.stopLocationUpdates();

        Log.d(TAG, "SerenghettoService.onDestroy");
    }


    private void sendNewBarcodesBroadcast(int newCodes) {
        Intent intent = new Intent(IntentConstants.NEW_BARCODES_INTENT);
        intent.putExtra(IntentConstants.NEW_BARCODES_EXTRA_COUNT, newCodes);
        
        /*[TODO: permissions]*/
        SerenghettoService.this.sendBroadcast(intent);
    }

    private class LocationHelper implements LocationListener {
        private static final int TIME_DELTA_SIGNIFICANT_WINDOW_MS = 1000 * 60 * 2; // 2 minutes

        private LocationManager locationManager;
        private Location bestLocationEstimate;

        public LocationHelper() {
            this.bestLocationEstimate = null;
            this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        public Location getLastKnownLocation() {
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        public void startLocationUpdates() {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        public void stopLocationUpdates() {
            locationManager.removeUpdates(this);
        }

        public Location getBestLocationEstimate() {
            return bestLocationEstimate;
        }
        private void setBestLocationEstimate(Location bestLocationEstimate) {
            this.bestLocationEstimate = bestLocationEstimate;

            app.setBestLocationEstimate(bestLocationEstimate);

            Intent intent = new Intent(IntentConstants.NEW_BEST_LOCATION_ESTIMATE_INTENT);
            intent.putExtra(IntentConstants.NEW_BEST_LOCATION_ESTIMATE_EXTRA_LOCATION, bestLocationEstimate);
            /*[TODO: permissions]*/
            SerenghettoService.this.sendBroadcast(intent);
        }

        // Methods required by LocationListener
        public void onLocationChanged(Location location) {
            //Log.d(TAG, "LOC: " + location.toString());
            if (isBetterLocation(location)) {
                //Log.d(TAG, "LOC better: " + location.toString());
                setBestLocationEstimate(location);
            }
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
            //[FIXME]
            //Toast.makeText(this, R.string.gps_disabled, Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public boolean isBetterLocation(Location location) {
            if (bestLocationEstimate == null) {
                return true;
            }

            // check whether the new location fix is newer
            long timeDelta = location.getTime() - bestLocationEstimate.getTime();
            boolean isSignificantlyNewer = timeDelta > TIME_DELTA_SIGNIFICANT_WINDOW_MS;
            boolean isSignificantlyOlder = timeDelta < -TIME_DELTA_SIGNIFICANT_WINDOW_MS;
            boolean isNewer = timeDelta > 0;

            if (isSignificantlyNewer) {
                return true;
            }
            else if (isSignificantlyOlder) {
                return false;
            }

            // check accuracy
            int accuracyDelta = (int)(location.getAccuracy() - bestLocationEstimate.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;

            boolean isFromSameProvider = isSameProvider(location.getProvider(), bestLocationEstimate.getProvider());

            if (isMoreAccurate) {
                return true;
            }
            else if (isNewer && !isLessAccurate) {
                return true;
            }
            else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }
            return false;
        }
        private boolean isSameProvider(String p1, String p2) {
            if (p1 == null) {
                return (p2 == null);
            }
            return p1.equals(p2);
        }
    }

    /**
    * Thread that performs the actual update from the online service
    */
    private class Updater extends Thread {
        List<Barcode> barcodes;

        public Updater() {
            super("SerenghettoService-Updater");

            barcodes = new ArrayList<Barcode>();
        }

        @Override
        public void run() {
            SerenghettoService service = SerenghettoService.this;
            SerenghettoApplication app = (SerenghettoApplication)service.getApplication();

            while (service.runFlag) {
                Log.d(TAG, "Updater running");
                try {
                    int newCodes = app.fetchBarcodes();
                    Log.d(TAG, newCodes + " new codes received");
                    if (newCodes > 0) {
                        SerenghettoService.this.sendNewBarcodesBroadcast(newCodes);
                    }
                    Thread.sleep(DELAY_MS);
                }
                catch (InterruptedException e) {
                    service.runFlag = false;
                }
            }
        }
    }
}


