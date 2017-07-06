package com.mc.app.hotel.bean;

/**
 * Created by Administrator on 2016/10/31.
 */
public class DataDemo {
    private int cinfo_id;

    private int cinfo_ctype_id;

    private String cinfo_code;

    private String cinfo_value;

    private String cinfo_date;

    private int cinfo_delete;

    public void setCinfo_id(int cinfo_id) {
        this.cinfo_id = cinfo_id;
    }

    public int getCinfo_id() {
        return this.cinfo_id;
    }

    public void setCinfo_ctype_id(int cinfo_ctype_id) {
        this.cinfo_ctype_id = cinfo_ctype_id;
    }

    public int getCinfo_ctype_id() {
        return this.cinfo_ctype_id;
    }

    public void setCinfo_code(String cinfo_code) {
        this.cinfo_code = cinfo_code;
    }

    public String getCinfo_code() {
        return this.cinfo_code;
    }

    public void setCinfo_value(String cinfo_value) {
        this.cinfo_value = cinfo_value;
    }

    public String getCinfo_value() {
        return this.cinfo_value;
    }

    public void setCinfo_date(String cinfo_date) {
        this.cinfo_date = cinfo_date;
    }

    public String getCinfo_date() {
        return this.cinfo_date;
    }

    public int getCinfo_delete() {
        return cinfo_delete;
    }

    public void setCinfo_delete(int cinfo_delete) {
        this.cinfo_delete = cinfo_delete;
    }

    @Override
    public String toString() {
        return "Result{" +
                "cinfo_id=" + cinfo_id +
                ", cinfo_ctype_id=" + cinfo_ctype_id +
                ", cinfo_code='" + cinfo_code + '\'' +
                ", cinfo_value='" + cinfo_value + '\'' +
                ", cinfo_date='" + cinfo_date + '\'' +
                ", cinfo_delete=" + cinfo_delete +
                '}';
    }
}
