package org.hiit.serenghetto;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class BarcodeData
{
    private static final String TAG = "serenghetto";

    final DbHelper dbHelper;
    private SQLiteDatabase db;

    /*[FIXME: better exceptions?]*/
    public BarcodeData(Context context) throws Exception {
        this.dbHelper = new DbHelper(context);
        this.db = this.dbHelper.getWritableDatabase();
        Log.i(TAG, "Initialized data");
    }
    public void close() {
        this.db.close();
        this.dbHelper.close();
    }

    public SQLiteDatabase getReadableDatabase() {
        return this.dbHelper.getReadableDatabase();
    }

    public Cursor getBarcodesByUser(String userId) {
        //SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Log.d(TAG, "selectBarcodesByUser: " + userId);
        Log.d(TAG, dbHelper.getQuery("barcodes_by_user"));

        try {
            String[] args = { userId };
            return db.rawQuery(dbHelper.getQuery("barcodes_by_user"), args);
        }
        catch (SQLiteException ex) {
            Log.d(TAG, ex.toString());
            return null;
        }
    }

    public boolean insertOrIgnoreBarcode(Barcode b) {
        Log.d(TAG, "insertOrIgnore on " + b);
        //SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        String[] args = { b.getId(), b.getUserId(), b.getCode(), b.getName() };
        boolean ret = true;

        //db.beginTransaction();
        try {
            db.execSQL(dbHelper.getQuery("insert_barcodes_basic"), args);
            //db.setTransactionSuccessful();
        }
        catch (SQLiteException ex) {
            Log.d(TAG, ex.toString());
            ret = false;
        }
        finally {
            //db.endTransaction();
            //db.close();
        }
        return ret;
    }
}

