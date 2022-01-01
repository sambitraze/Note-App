package com.xstudioo.noteme;

public class Note {
    private long id;
    private String title;
    private String content;
    private String date;
    private String time;
    private String status;

    Note(String title, String content, String date, String time, String status) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Note(long id, String title, String content, String date, String time, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    Note() {
        // empty constructor
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getStatus() {
        return status;
    }
}
