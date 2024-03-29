package fi.hiit.serenghetto.remote;

import android.os.AsyncTask;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

/**
*/
public abstract class ServerTask extends AsyncTask<String, Void, Response> {
    protected Response response;

    @Override
    protected abstract Response doInBackground(String... code);

    protected void onPostExecute(Response response) {
        this.response = response;
        this.handleResult();
        return;
    }

    protected abstract void handleResult();
}

