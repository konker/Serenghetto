package fi.hiit.serenghetto.dto;

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
        return "Barcode[id:" + id + ", userId:" + userId + ", code:" + code + ", name:" + name + ", latitude:" + latitude + ", longitude:" + longitude + ", accuracy:" + accuracy + ", timestamp:" + timestamp + "]";
    }
}

