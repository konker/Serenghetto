package fi.hiit.serenghetto.remote;

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

import fi.hiit.serenghetto.SerenghettoApplication;


public class RESTServer {
    private String userAgent;
    private String serverURL;
    private DefaultHttpClient httpClient;
    private String token;
    private String userId;


    public RESTServer(String userAgent, String url) {
        this.userAgent = userAgent;
        this.serverURL = url;
    }

    public String getUserResourceURL(String userId, String controller, String action) {
        String url = serverURL + "/api/" + userId + "/" + controller;
        if (action != null) {
            url += action;
        }
        return url;
    }
    public String getResourceURL(String controller, String action) {
        String url = serverURL + "/api/" + controller;
        if (action != null) {
            url += action;
        }
        return url;
    }

    protected Response _execGet(HttpGet req) {
        return _execGet(req, new ArrayList<BasicNameValuePair>());
    }
    protected Response _execGet(HttpGet req, List<BasicNameValuePair> nameValuePairs) {
        String params =  URLEncodedUtils.format(nameValuePairs, "UTF-8");
        String uri = req.getURI().toString();
        if (uri.indexOf("?") != -1) {
            uri = uri + "&" + params;
        }
        else {
            uri = uri + "?" + params;
        }
        Log.d(SerenghettoApplication.TAG, uri);

        try {
            req.setURI(new URI(uri));
        }
        catch (URISyntaxException ex) {
            Log.d(SerenghettoApplication.TAG, ex.toString());
            return new Response(500, "Error", null);
        }

        return _exec(req);
    }
    protected Response _execPost(HttpPost req) {
        return _execPost(req, new ArrayList<BasicNameValuePair>());
    }
    protected Response _execPost(HttpPost req, List<BasicNameValuePair> nameValuePairs) {
        Log.d(SerenghettoApplication.TAG, req.getURI().toString());
        try {
            for (BasicNameValuePair nv : nameValuePairs) {
                Log.d(SerenghettoApplication.TAG, nv.getName() + "->" + nv.getValue());
            }
            req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        catch (UnsupportedEncodingException ex) {
            Log.d(SerenghettoApplication.TAG, ex.toString());
            return new Response(500, "Error", null);
        }

        return _exec(req);
    }

    protected Response _exec(HttpRequestBase req) {
        req.setHeader("Accept", "application/json");
        try {
            //Log.d(SerenghettoApplication.TAG, req.toString());
            httpClient = new DefaultHttpClient();
            HttpResponse res = httpClient.execute(req);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            /*
            String json = new Scanner(reader).useDelimiter("\\A").next();
            Log.d(SerenghettoApplication.TAG, json);
            Response response = new Response(res.getStatusLine().getStatusCode(), json);
            */
            Response response = new Response(res.getStatusLine().getStatusCode(), reader);
            reader.close();
            //Log.d(SerenghettoApplication.TAG, response.toString());
            return response;
        }
        catch (IOException ex) {
            Log.d(SerenghettoApplication.TAG, ex.toString());
            return new Response(500, "Error", null);
        }
    }
}



