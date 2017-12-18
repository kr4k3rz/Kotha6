package com.codelite.kr4k3rz.kotha6.model;


import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    private String postId;
    private ArrayList<String> img_urls;
    private String city;
    private String address;
    private String rent_amt;
    private String available_date;
    private AvailableFor availableFor;
    private Amenities amenities;


    public Amenities getAmenities() {
        return amenities;
    }

    public void setAmenities(Amenities amenities) {
        this.amenities = amenities;
    }

    public AvailableFor getAvailableFor() {
        return availableFor;
    }

    public void setAvailableFor(AvailableFor availableFor) {
        this.availableFor = availableFor;
    }


    private String room_description;


    public ArrayList<String> getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(ArrayList<String> img_urls) {
        this.img_urls = img_urls;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRent_amt() {
        return rent_amt;
    }

    public void setRent_amt(String rent_amt) {
        this.rent_amt = rent_amt;
    }

    public String getAvailable_date() {
        return available_date;
    }

    public void setAvailable_date(String available_date) {
        this.available_date = available_date;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }
}