package fi.hiit.serenghetto.dto;

import android.util.Log;
import android.database.Cursor;
import org.json.simple.JSONObject;
import com.google.android.maps.GeoPoint;

import fi.hiit.serenghetto.SerenghettoApplication;


public class Barcode {
    private String id;
    private String userId;
    private String code;
    private String name;
    private double latitude;
    private double longitude;
    private double accuracy;
    private String timestamp;
    private double score;

    public Barcode(String id, String userId, String code, String name, double latitude, double longitude, double accuracy, String timestamp, double score) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.score = score;
    }

    public Barcode(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndex("_id"));
        this.userId = cursor.getString(cursor.getColumnIndex("user_id"));
        this.code = cursor.getString(cursor.getColumnIndex("code"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        this.longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        this.accuracy = cursor.getDouble(cursor.getColumnIndex("accuracy"));
        this.timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
        this.score = cursor.getDouble(cursor.getColumnIndex("score"));
    }

    public Barcode(JSONObject json) {
        try {
            JSONObject juser = (JSONObject)json.get("user");
            JSONObject jlocation = (JSONObject)json.get("location");
            JSONObject jscore = (JSONObject)json.get("score");

            /*[FIXME: hardcoded field names?]*/
            id = String.valueOf(json.get("id"));
            code = String.valueOf(json.get("code"));
            name = String.valueOf(json.get("name"));

            if (juser != null) {
                userId = String.valueOf(juser.get("id"));
            }
            if (jlocation != null) {
                Double jaccuracy = (Double)jlocation.get("accuracy");
                if (jaccuracy != null) {
                    accuracy = jaccuracy.doubleValue();
                }
                timestamp = String.valueOf(jlocation.get("device_timestamp"));

                JSONObject jgeom = (JSONObject)jlocation.get("geom");
                if (jgeom != null) {
                    Double jx = (Double)jgeom.get("x");
                    Double jy = (Double)jgeom.get("y");
                    latitude = jy.doubleValue();
                    longitude = jx.doubleValue();
                }
            }
            if (jscore != null) {
                Double jscore_score = (Double)jscore.get("score");
                if (jscore_score != null) {
                    score = jscore_score.doubleValue();
                }
            }
        }
        catch (Exception ex) {
            Log.d(SerenghettoApplication.TAG, "Barcode JSON contructor failed: " + ex);
        }
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean hasLocation() {
        return (latitude != 0 && longitude != 0);
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
    }

    public double getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }

    public String toString() {
        /*[FIXME: rest of fields]*/
        return "Barcode[id:" + id + ", userId:" + userId + ", code:" + code + ", name:" + name + ", latitude:" + latitude + ", longitude:" + longitude + ", accuracy:" + accuracy + ", timestamp:" + timestamp + ", score: " + score + "]";
    }
}

