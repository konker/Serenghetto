package fi.hiit.serenghetto.dto;

import android.util.Log;
import org.json.simple.JSONObject;

import fi.hiit.serenghetto.SerenghettoApplication;


public class Barcode {
    private String id;
    private String userId;
    private String code;
    private String name;
    private String latitude;
    private String longitude;
    private String accuracy;
    private String timestamp;
    private String score;

    public Barcode(String id, String userId, String code, String name, String latitude, String longitude, String accuracy, String timestamp, String score) {
        _init(id, userId, code, name, latitude, longitude, accuracy, timestamp, score);
    }

    public Barcode(String id, String userId, String code, String name) {
        _init(id, userId, code, name, null, null, null, null, null);
    }

    public Barcode(JSONObject json) {
        try {
            JSONObject juser = (JSONObject)json.get("user");
            JSONObject jlocation = (JSONObject)json.get("location");
            JSONObject jscore = (JSONObject)json.get("score");

            /*[FIXME: hardcoded field names?]*/
            id = String.valueOf(json.get("id"));
            code = (String)json.get("code");
            name = (String)json.get("name");

            if (juser != null) {
                userId = String.valueOf(juser.get("id"));
            }
            if (jlocation != null) {
                accuracy = String.valueOf(jlocation.get("accuracy"));
                timestamp = String.valueOf(jlocation.get("device_timestamp"));

                JSONObject jgeom = (JSONObject)jlocation.get("geom");
                if (jgeom != null) {
                    latitude = String.valueOf(jgeom.get("y"));
                    longitude = String.valueOf(jgeom.get("x"));
                }
            }
            if (jscore != null) {
                score = String.valueOf(jscore.get("score"));
            }
        }
        catch (Exception ex) {
            Log.d(SerenghettoApplication.TAG, "Barcode JSON contructor failed: " + ex);
        }
    }

    private void _init(String id, String userId, String code, String name, String latitude, String longitude, String accuracy, String timestamp, String score) {
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

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }

    public String toString() {
        /*[FIXME: rest of fields]*/
        return "Barcode[id:" + id + ", userId:" + userId + ", code:" + code + ", name:" + name + ", latitude:" + latitude + ", longitude:" + longitude + ", accuracy:" + accuracy + ", timestamp:" + timestamp + ", score: " + score + "]";
    }
}

