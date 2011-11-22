package fi.hiit.serenghetto.data;

import android.util.Log;
import java.util.Iterator;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import fi.hiit.serenghetto.dto.Barcode;
import fi.hiit.serenghetto.SerenghettoApplication;
import fi.hiit.serenghetto.remote.SerenghettoServer;
import fi.hiit.serenghetto.remote.Response;


public class BarcodeData
{
    private final DbHelper dbHelper;
    private SQLiteDatabase db;

    /*[FIXME: better exceptions?]*/
    public BarcodeData(Context context) throws Exception {
        this.dbHelper = new DbHelper(context);
        this.db = this.dbHelper.getWritableDatabase();
        Log.i(SerenghettoApplication.TAG, "Initialized data");
    }
    public void close() {
        this.db.close();
        this.dbHelper.close();
    }

    public SQLiteDatabase getReadableDatabase() {
        return this.dbHelper.getReadableDatabase();
    }

    public Cursor getBarcodesByUser(String userId) {
        Log.d(SerenghettoApplication.TAG, "selectBarcodesByUser: " + userId);
        try {
            String[] args = { userId };
            return db.rawQuery(dbHelper.getQuery("barcodes_by_user"), args);
        }
        catch (SQLiteException ex) {
            Log.d(SerenghettoApplication.TAG, ex.toString());
            return null;
        }
    }

    public Cursor getBarcodeById(String id) {
        Log.d(SerenghettoApplication.TAG, "selectBarcodeById: " + id);
        Log.d(SerenghettoApplication.TAG, dbHelper.getQuery("barcode_by_id"));

        try {
            String[] args = { id };
            return db.rawQuery(dbHelper.getQuery("barcode_by_id"), args);
        }
        catch (SQLiteException ex) {
            Log.d(SerenghettoApplication.TAG, ex.toString());
            return null;
        }
    }

    /*[FIXME: should this be moved into BarcodeData? Somewhere else?]*/
    public int synchBarcodesWithServer(SerenghettoServer server) {
        int count = 0;
        Response response = server.getBarcodes();

        if (response.getHttpCode() != 500) {
            JSONObject body = (JSONObject)response.getBody();
            if (body != null) {
                JSONArray codes = (JSONArray)response.getBody().get("entries");
                for (Iterator iter = codes.iterator(); iter.hasNext();) {
                    JSONObject json = (JSONObject)iter.next();
                    Barcode barcode = new Barcode(json);

                    boolean inserted = insertOrUpdateBarcode(barcode);
                    if (inserted) {
                        count = count + 1;
                    }
                }
            }
        }
        else {
            Log.i(SerenghettoApplication.TAG, "No codes returned");
        }
        return count;
    }
    
    public boolean insertOrUpdateBarcode(Barcode b) {
        Log.d(SerenghettoApplication.TAG, "insertOrUpdateBarcode on " + b);

        boolean ret = true;

        try {
            String[] args = {
                b.getId(),
                b.getUserId(),
                b.getCode(),
                b.getName(),
                String.valueOf(b.getLatitude()),
                String.valueOf(b.getLongitude()),
                String.valueOf(b.getAccuracy()),
                b.getTimestamp(),
                String.valueOf(b.getScore())
                };
            db.execSQL(dbHelper.getQuery("insert_barcodes"), args);
        }
        catch (SQLiteException ex1) {
            ret = false;
            try {
                String[] args = {
                    b.getUserId(),
                    b.getCode(),
                    b.getName(),
                    String.valueOf(b.getLatitude()),
                    String.valueOf(b.getLongitude()),
                    String.valueOf(b.getAccuracy()),
                    b.getTimestamp(),
                    String.valueOf(b.getScore()),
                    b.getId()
                    };
                db.execSQL(dbHelper.getQuery("update_barcode"), args);
            }
            catch (SQLiteException ex2) {
                Log.d(SerenghettoApplication.TAG, ex2.toString());
            }
        }
        return ret;
    }
}

