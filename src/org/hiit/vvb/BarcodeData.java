package org.hiit.vvb;

import android.util.Log;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BarcodeData
{
    private static final String TAG = "VVB";

    static final String NAME_QUERY = "query";
    static final String NAME_NAME = "name";

    static final int DB_VERSION = 2;
    static final String DATABASE = "barcodes.db";
    //static final String TABLE = "barcodes";


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

    public Cursor getBarcodesByUser(String userId) {
        Log.d(TAG, "selectBarcodesByUser: " + userId);

        try {
            String[] args = { userId };
            return db.rawQuery(dbHelper.getQuery("barcodes_by_user"), args);
        }
        catch (SQLiteException ex) {
            return null;
        }
        /*
        finally {
            db.close();
        }
        */
    }

    public boolean insertBarcode(Barcode b) {
        Log.d(TAG, "insertBarcode: " + b);
        /*
        Log.d(TAG, "c_id:" + dbHelper.getQuery("c_id"));
        Log.d(TAG, "c_user_id:" + dbHelper.getQuery("c_user_id"));
        Log.d(TAG, "c_code:" + dbHelper.getQuery("c_code"));
        Log.d(TAG, "c_name:" + dbHelper.getQuery("c_name"));

        ContentValues values = new ContentValues();
        values.put(dbHelper.getQuery("c_id"), b.getId());
        values.put(dbHelper.getQuery("c_user_id"), b.getUserId());
        values.put(dbHelper.getQuery("c_code"), b.getCode());
        values.put(dbHelper.getQuery("c_name"), b.getName());
        return insertOrIgnore(values);
        */
        return insertOrIgnore(b);
    }

    //public boolean insertOrIgnore(ContentValues values) {
    public boolean insertOrIgnore(Barcode b) {
        //Log.d(TAG, "insertOrIgnore on " + values);
        //Log.d(TAG, "insertOrIgnore on " + b);
        //SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        boolean ret = true;
        try {
            //db.insertOrThrow(dbHelper.getQuery(DbHelper.BARCODES_NAME), null, values);
            String[] args = { b.getId(), b.getUserId(), b.getCode(), b.getName() };
            db.rawQuery(dbHelper.getQuery("insert_barcodes_basic"), args);
        }
        catch (SQLiteException ex) {
            Log.d(TAG, ex.toString());
            ret = false;
        }
        /*
        finally {
            db.close();
        }
        */
        return ret;
    }

    // DbHelper implementations
    class DbHelper extends SQLiteOpenHelper
    {
        public static final String BARCODES_NAME = "barcodes_name";
        public static final String CREATE_BARCODES = "create_barcodes";
        public static final String DROP_BARCODES = "drop_barcodes";

        private DbXmlHelper dbQueries;

        /*[FIXME: better exceptions?]*/
        public DbHelper(Context context) throws Exception {
            super(context, DATABASE, null, DB_VERSION);
            this.dbQueries = new DbXmlHelper(context);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating database: " + DATABASE);
            db.execSQL(dbQueries.getQuery(CREATE_BARCODES));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(dbQueries.getQuery(DROP_BARCODES));
            this.onCreate(db);
        }

        public String getQuery(String name) {
            return dbQueries.getQuery(name);
        }
    }
    
    /* XML parser */
    class DbXmlHelper extends DefaultHandler
    {
        private HashMap<String, String> rep;
        private String curName = null;
        private String curValue = null;

        /*[FIXME: better exceptions?]*/
        public DbXmlHelper(Context context) throws Exception {
            this.rep = new HashMap<String, String>();
            
            // read in and parse the xml
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            xr.setContentHandler(this);
            xr.parse(new InputSource(context.getResources().openRawResource(R.raw.db)));
            Log.d(TAG, "p:parsed: " + rep);
        }

        public String getQuery(String name) {
            return rep.get(name);
        }

        public void setQuery(String name, String sql) {
            Log.d(TAG, "setQuery: " + name + ", " + sql);
            rep.put(name, sql);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            Log.d(TAG, "p:startElement: " + (localName.equals(NAME_QUERY)));
            if (localName.equals(NAME_QUERY)) {
                Log.d(TAG, "p:attr:" + attributes.getValue(NAME_NAME));
                curName = attributes.getValue(NAME_NAME);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            Log.d(TAG, "p:endElement: " + curName);
            if (localName.equalsIgnoreCase(NAME_QUERY)) {
                if (curName != null && curValue != null) {
                    setQuery(curName, curValue);
                }
                curValue = null;
                curName = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            Log.d(TAG, "p:characters: " + length + ": " + curName);
            if (curName != null) {
                if (curValue == null) {
                    curValue = "";
                }
                curValue += new String(ch, start, length);
            }
        }
    }
}

