package hcmute.edu.vn.tlcn.Models;

public class Notice {

    private String content, day, time;

    public Notice(String content, String day, String time) {
        this.content = content;
        this.day = day;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
