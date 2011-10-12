package org.hiit.vvb;

import java.io.Reader;
import java.util.Map;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

public class Response {
    private int httpCode;
    private String message;
    private Map body;

    public Response(int httpCode, Reader in) {
        JSONObject response = (JSONObject)JSONValue.parse(in);
        this.httpCode = httpCode;
        this.message = (String)response.get("message");
        this.body = (Map)response.get("body");
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


