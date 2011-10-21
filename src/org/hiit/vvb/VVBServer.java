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

    private String serverURL;
    private DefaultHttpClient httpClient;
    private String token;


    public VVBServer(String url, String token) {
        this.serverURL = url;
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        Log.d(TAG, "token: " + token);
        this.token = token;
    }

    public Response getCodes() {
        BasicHttpParams params = new BasicHttpParams();
        params.setParameter("token", token);

        HttpGet req = new HttpGet(getResourceURL("barcode", null));
        req.setHeader("Accept", "application/json");
        req.setParams(params);

        StringBuilder json = new StringBuilder();
        try {
            httpClient = new DefaultHttpClient();
            HttpResponse res = httpClient.execute(req);
            
            //return res.getEntity().getContent().read();
            //return IOUtils.toString(myInputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            Response response = new Response(res.getStatusLine().getStatusCode(), reader);
            /*
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }
            */
            //return json.toString();
            return response;
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }
    }

    public Response postCode(String code, String name, String latitude, String longitude, String accuracy, String timestamp) {
        //[FIXME: needs real location]
        HttpPost req = new HttpPost(getResourceURL("barcode", null));
        req.setHeader("Accept", "application/json");

        List nameValuePairs = new ArrayList(1);
        nameValuePairs.add(new BasicNameValuePair("barcode[code]", code));
        nameValuePairs.add(new BasicNameValuePair("barcode[name]", name));
        nameValuePairs.add(new BasicNameValuePair("location[latitude]", latitude));
        nameValuePairs.add(new BasicNameValuePair("location[longitude]", longitude));
        nameValuePairs.add(new BasicNameValuePair("location[accuracy]", accuracy));
        nameValuePairs.add(new BasicNameValuePair("location[timestamp]", timestamp));
        //nameValuePairs.add(new BasicNameValuePair("nmea[string]", nmeaString));
        //nameValuePairs.add(new BasicNameValuePair("nmea[timestamp]", nmeaTimestamp));
        nameValuePairs.add(new BasicNameValuePair("token", token));

        try {
            req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }

        try {
            httpClient = new DefaultHttpClient();
            HttpResponse res = httpClient.execute(req);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            Response response = new Response(res.getStatusLine().getStatusCode(), reader);
            return response;
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }
    }

    public Response getToken(String email, String password) {
        HttpPost req = new HttpPost(getResourceURL("session", null));
        req.setHeader("Accept", "application/json");

        List nameValuePairs = new ArrayList(1);
        nameValuePairs.add(new BasicNameValuePair("user[email]", email));
        nameValuePairs.add(new BasicNameValuePair("user[password]", password));

        try {
            req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }

        StringBuilder json = new StringBuilder();
        try {
            httpClient = new DefaultHttpClient();
            HttpResponse res = httpClient.execute(req);

            // read response into a StringBuilder
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            Response response = new Response(res.getStatusLine().getStatusCode(), reader);
            /*
            String line;
            while ((line = r.readLine()) != null) {
                json.append(line);
            }
            */
            //return json.toString();
            return response;
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }
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


