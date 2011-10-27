package org.hiit.vvb;

public class Barcode {
    private String id;
    private String userId;
    private String code;
    private String name;
    private String createdAt;
    private String updatedAt;
    private String lat;
    private String lng;
    private String accuracy;

    public Barcode(String id, String userId, String code, String name, String createdAt, String updatedAt, String lat, String lng, String accuracy) {
        _init(id, userId, code, name, createdAt, updatedAt, lat, lng, accuracy);
    }

    public Barcode(String id, String userId, String code, String name) {
        _init(id, userId, code, name, null, null, null, null, null);
    }

    private void _init(String id, String userId, String code, String name, String createdAt, String updatedAt, String lat, String lng, String accuracy) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lat = lat;
        this.lng = lng;
        this.accuracy = accuracy;
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

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        /*[FIXME: rest of fields]*/
        return "Barcode[id:" + id + ", userId:" + userId + ", code:" + code + ", name:" + name + "]";
    }
}

