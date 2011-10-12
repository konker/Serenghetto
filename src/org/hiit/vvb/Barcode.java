package org.hiit.vvb;

public class Barcode {
    private String code;
    private String name;

    public Barcode(String code, String name) {
        this.code = code;
        this.name = name;
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
        return "Barcode[" + code + "]: " + name;
    }
}

