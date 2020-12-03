package com.getrentbd.getrent.modelClass;

public class FavoriteItemsList {
    private String id;
    private String seat;
    private String num_floor;
    private String location;
    private String month;
    private String rent;
    private String catagory;
    private String timeDate;
    private String url;
    private String address;
    private String facilities;
    private String name;
    private String number;
    private String email;
    private String gender;

    public FavoriteItemsList(String id, String seat, String num_floor, String location,
                             String month, String rent, String catagory, String timeDate,
                             String url,String address, String facilities, String name,
                             String number, String email,String gender) {
        this.id = id;
        this.seat = seat;
        this.num_floor = num_floor;
        this.location = location;
        this.month = month;
        this.rent = rent;
        this.catagory = catagory;
        this.timeDate = timeDate;
        this.url = url;
        this.address = address;
        this.facilities = facilities;
        this.name = name;
        this.number = number;
        this.email = email;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getNum_floor() {
        return num_floor;
    }

    public void setNum_floor(String num_floor) {
        this.num_floor = num_floor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return url;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }
}
