/**
*/

package org.hiit.serenghetto;

import android.util.Log;
import android.net.http.AndroidHttpClient;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.message.BasicNameValuePair;

import java.util.Scanner;


public class SerenghettoServer {
    private static final String TAG = "SERENGHETTO";
    private static final String TOKEN_NAME = "token";

    //[FIXME: better ua]
    public static String CLIENT_USER_AGENT = "Android SERENGHETTO...";

    private String serverURL;
    private DefaultHttpClient httpClient;
    private String token;
    private String userId;


    public SerenghettoServer(String url, String token, String userId) {
        this.serverURL = url;
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        Log.d(TAG, "token: " + token);
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        Log.d(TAG, "userId: " + userId);
        this.userId = userId;
    }

    public Response getBarcodes() {
        HttpGet req = new HttpGet(getResourceURL("barcodes", null));
        return _execGet(req);
    }

    public Response postCode(String code, String name, String latitude, String longitude, String accuracy, String timestamp) {
        HttpPost req = new HttpPost(getResourceURL("barcode", null));

        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("barcode[code]", code));
        nameValuePairs.add(new BasicNameValuePair("barcode[name]", name));
        nameValuePairs.add(new BasicNameValuePair("location[latitude]", latitude));
        nameValuePairs.add(new BasicNameValuePair("location[longitude]", longitude));
        nameValuePairs.add(new BasicNameValuePair("location[accuracy]", accuracy));
        nameValuePairs.add(new BasicNameValuePair("location[timestamp]", timestamp));
        nameValuePairs.add(new BasicNameValuePair("token", token));

        return _execPost(req, nameValuePairs);
    }

    public Response authorzie(String email, String password) {
        // set the token to null so that it isn't sent with this request
        token = null;
        HttpPost req = new HttpPost(getResourceURL("session", null));

        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user[email]", email));
        nameValuePairs.add(new BasicNameValuePair("user[password]", password));

        return _execPost(req, nameValuePairs);
    }


    /* HELPERS */
    public String getUserResourceURL(String userId, String controller, String action) {
        String url = serverURL + "/api/" + userId + "/" + controller;
        if (action != null) {
            url += action;
        }
        Log.d(TAG, url);
        return url;
    }
    public String getResourceURL(String controller, String action) {
        String url = serverURL + "/api/" + controller;
        if (action != null) {
            url += action;
        }
        Log.d(TAG, url);
        return url;
    }

    protected Response _execGet(HttpGet req) {
        return _execGet(req, new ArrayList<BasicNameValuePair>());
    }
    protected Response _execGet(HttpGet req, List<BasicNameValuePair> nameValuePairs) {
        // automatically add token
        if (token != null) {
            nameValuePairs.add(new BasicNameValuePair(TOKEN_NAME, token));
        }

        String params =  URLEncodedUtils.format(nameValuePairs, "UTF-8");
        String uri = req.getURI().toString();
        if (uri.indexOf("?") != -1) {
            uri = uri + "&" + params;
        }
        else {
            uri = uri + "?" + params;
        }
        Log.d(TAG, uri);

        try {
            req.setURI(new URI(uri));
        }
        catch (URISyntaxException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }

        return _exec(req);
    }
    protected Response _execPost(HttpPost req) {
        return _execPost(req, new ArrayList<BasicNameValuePair>());
    }
    protected Response _execPost(HttpPost req, List<BasicNameValuePair> nameValuePairs) {
        // automatically add token
        if (token != null) {
            nameValuePairs.add(new BasicNameValuePair(TOKEN_NAME, token));
        }

        try {
            for (BasicNameValuePair nv : nameValuePairs) {
                Log.d(TAG, nv.getName() + "->" + nv.getValue());
            }
            req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }

        return _exec(req);
    }

    protected Response _exec(HttpRequestBase req) {
        req.setHeader("Accept", "application/json");
        try {
            Log.d(TAG, req.toString());
            httpClient = new DefaultHttpClient();
            HttpResponse res = httpClient.execute(req);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            /*
            String json = new Scanner(reader).useDelimiter("\\A").next();
            Log.d(TAG, json);
            Response response = new Response(res.getStatusLine().getStatusCode(), json);
            */
            Response response = new Response(res.getStatusLine().getStatusCode(), reader);
            reader.close();
            Log.d(TAG, response.toString());
            return response;
        }
        catch (IOException ex) {
            Log.d(TAG, ex.toString());
            return new Response(500, "Error", null);
        }
    }
}


