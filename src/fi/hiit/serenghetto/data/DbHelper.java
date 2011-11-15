package fi.hiit.serenghetto.data;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// DbHelper implementations
class DbHelper extends SQLiteOpenHelper
{
    private static final String TAG = "SERENGHETTO";

    static final int DB_VERSION = 5;
    static final String DB_NAME = "barcodes.db";

    private DbXmlParser dbQueries;
    private Context context;

    /*[FIXME: better exceptions?]*/
    public DbHelper(Context context) throws Exception {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.dbQueries = new DbXmlParser(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating database: " + DB_NAME);
        db.execSQL(dbQueries.getQuery("create_barcodes"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*[FIXME: this should be ALTER TABLE .. statements rather than DROP]*/
        db.execSQL(dbQueries.getQuery("drop_barcodes"));
        this.onCreate(db);
    }

    public String getQuery(String name) {
        return dbQueries.getQuery(name);
    }
}

