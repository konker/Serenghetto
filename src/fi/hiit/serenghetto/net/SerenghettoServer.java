package fi.hiit.serenghetto.net;

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


public class SerenghettoServer extends RESTServer {
    private static final String TAG = "SERENGHETTO";
    private static final String TOKEN_NAME = "token";

    //[FIXME: better ua]
    public static String CLIENT_USER_AGENT = "Android SERENGHETTO...";

    private String token;
    private String userId;


    public SerenghettoServer(String url, String token, String userId) {
        super(CLIENT_USER_AGENT, url);
        this.token = token;
        this.userId = userId;
        Log.d(TAG, "SerenghettoServer.construct: " + url + "," + token + "," + userId);
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

    public Response postBarcode(String code, String name, String latitude, String longitude, String accuracy, String timestamp) {
        HttpPost req = new HttpPost(getResourceURL("barcode", null));

        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("barcode[code]", code));
        nameValuePairs.add(new BasicNameValuePair("barcode[name]", name));
        nameValuePairs.add(new BasicNameValuePair("location[latitude]", latitude));
        nameValuePairs.add(new BasicNameValuePair("location[longitude]", longitude));
        nameValuePairs.add(new BasicNameValuePair("location[accuracy]", accuracy));
        nameValuePairs.add(new BasicNameValuePair("location[timestamp]", timestamp));
        nameValuePairs.add(new BasicNameValuePair("token", token));

        // add token
        if (token != null) {
            nameValuePairs.add(new BasicNameValuePair(TOKEN_NAME, token));
        }


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
}


