package com.example.signup.Notifications;

public class Sender {
    public Data data;
    public String to;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Sender(Data notification, String to) {
        this.data = notification;
        this.to = to;
    }
}
