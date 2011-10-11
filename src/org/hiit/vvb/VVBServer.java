/**
*/

package org.hiit.vvb;

import android.util.Log;
import android.net.http.AndroidHttpClient;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;


public class VVBServer {
    private static final String TAG = "VVB";
    //[FIXME: better ua]
    public static String CLIENT_USER_AGENT = "Android VVB...";

    String serverURL;
    DefaultHttpClient httpClient;
    String authToken;


    public VVBServer(String url, String authToken) {
        serverURL = url;
        authToken = authToken;
        httpClient = new DefaultHttpClient();
    }

    public String getCodes() {
        BasicHttpParams params = new BasicHttpParams();
        params.setParameter("_token", authToken);

        HttpGet req = new HttpGet(getResourceURL("barcode", null));
        req.setHeader("Accept", "text/json");
        req.setParams(params);

        StringBuilder json = new StringBuilder();
        try {
            HttpResponse res = httpClient.execute(req);
            
            //return res.getEntity().getContent().read();
            //return IOUtils.toString(myInputStream, "UTF-8");
            BufferedReader r = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return ex.toString();
        }
        return json.toString();
    }

    public String postCode(String code, String latitude, String longitude, String accuracy) {
        //[FIXME: needs real location]
        HttpPost req = new HttpPost(getResourceURL("barcode", null));
        req.setHeader("Accept", "text/json");

        List nameValuePairs = new ArrayList(1);
        nameValuePairs.add(new BasicNameValuePair("code", code));
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("accuracy", accuracy));
        nameValuePairs.add(new BasicNameValuePair("_token", authToken));

        try {
            req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) {
            Log.d(TAG, ex.toString());
            return "ERR1";
        }

        StringBuilder json = new StringBuilder();
        try {
            HttpResponse res = httpClient.execute(req);
            
            //return res.getEntity().getContent().read();
            //return IOUtils.toString(myInputStream, "UTF-8");
            BufferedReader r = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return ex.toString();
        }
        return json.toString();
    }

    public String getToken(String username, String password) {
        HttpPost req = new HttpPost(getResourceURL("session", null));
        req.setHeader("Accept", "text/json");

        List nameValuePairs = new ArrayList(1);
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));

        try {
            req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) {
            Log.d(TAG, ex.toString());
            return "ERR1";
        }

        StringBuilder json = new StringBuilder();
        try {
            HttpResponse res = httpClient.execute(req);

            // read response into a StringBuilder
            BufferedReader r = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return ex.toString();
        }
        return json.toString();
    }

    public String getResourceURL(String controller, String action) {
        String url = serverURL + "/api/" + controller;
        if (action != null) {
            url += action;
        }
        Log.d(TAG, url);
        return url;
    }
}


