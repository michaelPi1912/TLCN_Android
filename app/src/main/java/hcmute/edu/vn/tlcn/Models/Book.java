package hcmute.edu.vn.tlcn.Models;

import java.io.Serializable;

public class Book implements Serializable {
    String startpoint, endpoint, date, time, type, hangbay;

    public Book(String startpoint, String endpoint, String date, String time, String type, String hangbay) {
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.date = date;
        this.time = time;
        this.type = type;
        this.hangbay = hangbay;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHangbay() {
        return hangbay;
    }

    public void setHangbay(String hangbay) {
        this.hangbay = hangbay;
    }
}
