package org.hiit.vvb;

import android.app.Activity;
import android.os.Bundle;

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.AndroidHttpClient;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class VVBTestCodes extends Activity implements OnClickListener
{
    private AndroidHttpClient httpClient;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codes);

        // set a click listener on the scan_code button
        Button scan_code = (Button)findViewById(R.id.scan_code);
        scan_code.setOnClickListener(this);

        // get a http client instance
        httpClient = AndroidHttpClient.newInstance("VVBTest android");
    }

    public void onClick(View view) {
        if (view == findViewById(R.id.scan_code)) {
            AlertDialog ad = IntentIntegrator.initiateScan(this);
            /*
            if (ad == null) {
                alert("Say \"Code be scanned\". Say it. Say \"Code be scanned\".: " + ad);
            }
            */

            /*
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            intent.putExtra("SCAN_WIDTH", 800);
            intent.putExtra("SCAN_HEIGHT", 200);
            startActivityForResult(intent, 0);
            */
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            alert("Format: " + scanResult.getFormatName() + "\nContents: " + scanResult.getContents());
        }
        
        /*
        alert("resultCode: " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                alert("Format: " + format + "\nContents: " + contents);

                try {
                    send(format, contents);
                }
                catch(IOException e) {
                    alert("send failed: " + e);
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                alert("failed");
            }
        }
        */
    }
    
    private void send(String format, String code) throws IOException {
        URL url = null;
        try {
            url = new URL("http://blob.a-z.fi/?f=" + format + "&c=" + code);
        }
        catch (MalformedURLException e) {
            throw new IOException("Could not connect to server");
        }

        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                conn.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
    
        alert(inputLine);
    }

    private void alert(CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    /*
    public void alert(String s) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage(s);

        // add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });

        alertbox.show();
    }
    */
}

