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

public class BarcodeData {
    private static final String TAG = "VVB";

    static final String NAME_DB = "db";
    static final String NAME_NAME = "name";

    static final int VERSION = 1;
    static final String DATABASE = "barcodes.db";
    static final String TABLE = "barcodes";

    // DBXML parser
    class DbXmlParser extends DefaultHandler
    {
        private HashMap<String, String> rep;
        private String curName = null;
        private String curValue = null;

        /*[FIXME: better exceptions?]*/
        public DbXmlParser(Resources res) throws Exception {
            rep = new HashMap<String, String>();
            
            // read in and parse the xml
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            xr.setContentHandler(this);
            xr.parse(new InputSource(res.openRawResource(R.raw.db)));
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

    // DbHelper implementations
    class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE, null, VERSION);
            //Xml.parse(getResources().openRawResource(R.raw.testXML), Xml.Encoding.UTF_8, root.getContentHandler());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating database: " + DATABASE);
            db.execSQL("");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("");
            this.onCreate(db);
        }
    }
    
}

