package com.iabur.kafkademo.model;

import java.util.Date;

public class ErrorMessage {
    private Date date;
    private String message;
    private String details;

    public ErrorMessage(Date date, String message, String s) {
        this.date = date;
        this.message = message;
        this.details = s;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}