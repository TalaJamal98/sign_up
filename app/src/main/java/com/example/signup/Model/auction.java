package com.example.signup.Model;

public class auction {

    private String name;
    private String category;
    private String price;
    private String time;
    private String date;
    private String Publisher;
    private String description;
    private String AuctionId;

    public auction(String name, String category, String price, String time, String date, String publisher, String description, String auctionId) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.time = time;
        this.date = date;
        Publisher = publisher;
        this.description = description;
        AuctionId = auctionId;
    }

    public auction(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuctionId() {
        return AuctionId;
    }

    public void setAuctionId(String auctionId) {
        AuctionId = auctionId;
    }
}
