package fi.hiit.serenghetto.data;

import android.util.Log;
import android.content.Context;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fi.hiit.serenghetto.R;

/* XML parser */
class DbXmlParser extends DefaultHandler
{
    private static final String TAG = "serenghetto";

    static final String NAME_QUERY = "query";
    static final String NAME_NAME = "name";

    private HashMap<String, String> rep;
    private String curName = null;
    private String curValue = null;

    /*[FIXME: better exceptions?]*/
    public DbXmlParser(Context context) throws Exception {
        this.rep = new HashMap<String, String>();

        // read in and parse the xml
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();

        xr.setContentHandler(this);
        xr.parse(new InputSource(context.getResources().openRawResource(R.raw.db)));
        //Log.d(TAG, "p:parsed: " + rep);
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
        //Log.d(TAG, "p:startElement: " + (localName.equals(NAME_QUERY)));
        if (localName.equals(NAME_QUERY)) {
            //Log.d(TAG, "p:attr:" + attributes.getValue(NAME_NAME));
            curName = attributes.getValue(NAME_NAME);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //Log.d(TAG, "p:endElement: " + curName);
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
        //Log.d(TAG, "p:characters: " + length + ": " + curName);
        if (curName != null) {
            if (curValue == null) {
                curValue = "";
            }
            curValue += new String(ch, start, length);
        }
    }
}

