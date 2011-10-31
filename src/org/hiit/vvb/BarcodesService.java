package org.hiit.vvb;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import android.app.Service; 
import android.content.Intent; 
import android.os.IBinder; 
import android.util.Log;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


public class BarcodesService extends Service {
    private static final String TAG = "VVB";
    static final int DELAY_MS = 60000;
    private boolean runFlag = false;
    private Updater updater;
    private VVBApplication app;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.app = (VVBApplication) getApplication();
        this.updater = new Updater();
        Log.d(TAG, "onCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        this.runFlag = true;
        this.updater.start();
        this.app.setServiceRunning(true);
        Log.d(TAG, "onStarted");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runFlag = false;
        this.updater.interrupt();
        this.updater = null;
        this.app.setServiceRunning(false);
        Log.d(TAG, "onDestroyed");
    }

    /**
    * Thread that performs the actual update from the online service
    */
    private class Updater extends Thread {
        List<Barcode> barcodes;

        public Updater() {
            super("BarcodesService-Updater");

            barcodes = new ArrayList<Barcode>();
        }

        @Override
        public void run() {
            BarcodesService service = BarcodesService.this;
            while (service.runFlag) {
                Log.d(TAG, "Updater running");
                try {
                    VVBApplication app = (VVBApplication)service.getApplication();
                    int newCodes = app.fetchBarcodes();
                    if (newCodes > 0) {
                        Log.d(TAG, newCodes + " new codes received");
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

