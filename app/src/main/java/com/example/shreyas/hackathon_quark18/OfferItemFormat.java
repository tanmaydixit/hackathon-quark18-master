package com.example.shreyas.hackathon_quark18;


public class OfferItemFormat {
    String code;
    String desc;

    public OfferItemFormat(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public OfferItemFormat() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
