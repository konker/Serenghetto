package org.hiit.vvb;

import android.util.Log;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    static final String NAME_DB = "db";
    static final String NAME_NAME = "name";

    static final int VERSION = 1;
    static final String DATABASE = "barcodes.db";
    static final String TABLE = "barcodes";


    final DbHelper dbHelper;

    /*[FIXME: better exceptions?]*/
    public BarcodeData(Context context) throws Exception {
        this.dbHelper = new DbHelper(context);
        Log.i(TAG, "Initialized data");
    }
    public void close() {
        this.dbHelper.close();
    }

    // DbHelper implementations
    class DbHelper extends SQLiteOpenHelper
    {
        public static final String CREATE_BARCODES = "create_barcodes";
        public static final String DROP_BARCODES = "drop_barcodes";

        private DbXmlHelper dbQueries;

        /*[FIXME: better exceptions?]*/
        public DbHelper(Context context) throws Exception {
            super(context, DATABASE, null, VERSION);
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
        }

        public String getQuery(String name) {
            return rep.get(name);
        }

        public void setQuery(String name, String sql) {
            rep.put(name, sql);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals(NAME_DB)) {
                curName = attributes.getValue(NAME_NAME);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equalsIgnoreCase(NAME_DB)) {
                if (curName != null && curValue != null) {
                    setQuery(curName, curValue);
                }
                curValue = null;
                curName = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (curName != null) {
                if (curValue == null) {
                    curValue = "";
                }
                curValue += new String(ch, start, length);
            }
        }
    }
}

