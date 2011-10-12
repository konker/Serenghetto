package org.hiit.vvb;

import android.os.AsyncTask;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

/**
*/
public abstract class VVBServerTask extends AsyncTask<String, Void, Response> {
    protected Response response;

    @Override
    protected abstract Response doInBackground(String... code);

    protected void onPostExecute(Response response) {
        /*
        JSONObject result = (JSONObject)JSONValue.parse(json);
        if (result != null) {
            this.msg = (String)result.get("msg");
            this.body = (JSONObject)result.get("body");
        }
        */
        this.response = response;
        this.handleResult();
        return;
    }

    protected abstract void handleResult();
}

