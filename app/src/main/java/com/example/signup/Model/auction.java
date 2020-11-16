package com.example.signup.Model;

public class auction {
    private String imageurl;
private String buyer;
    private String name;
    private String category;
    private String price;
    private String startTime;
    private String endTime;
    private String date;
    private String Publisher;
    private String description;
    private String AuctionId;
    public auction(){

    }

    public auction(String imageurl, String buyer, String name, String category, String price, String startTime, String endTime, String date, String publisher, String description, String auctionId) {
        this.imageurl = imageurl;
        this.buyer = buyer;
        this.name = name;
        this.category = category;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        Publisher = publisher;
        this.description = description;
        AuctionId = auctionId;
    }


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
