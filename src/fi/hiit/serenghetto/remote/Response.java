package fi.hiit.serenghetto.remote;

import java.io.Reader;
import java.util.Map;
import java.util.HashMap;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import android.util.Log; 

public class Response {
    private int httpCode;
    private String message;
    private Map body;

    public Response(int httpCode, Reader in) {
        JSONObject response = (JSONObject)JSONValue.parse(in);
        _initFromJSON(httpCode, response);
    }
    public Response(int httpCode, String in) {
        JSONObject response = (JSONObject)JSONValue.parse(in);
        _initFromJSON(httpCode, response);
    }
    private void _initFromJSON(int httpCode, JSONObject response) {
        if (response != null) {
            this.httpCode = httpCode;
            this.message = (String)response.get("message");
            this.body = (Map)response.get("body");
        }
        else {
            this.httpCode = 500;
            this.message = "An error has occurred.";
            this.body = new HashMap();
        }
    }

    public Response(int httpCode, String message, Map body) {
        this.httpCode = httpCode;
        this.message = message;
        this.body = body;
    }

    public int getHttpCode() {
        return httpCode;
    }
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Map getBody() {
        return body;
    }
    public void setBody(Map bpdy) {
        this.body = body;
    }
    public String toString() {
        return "Response[" + httpCode + "][" + message + "]: " + body;
    }
}


