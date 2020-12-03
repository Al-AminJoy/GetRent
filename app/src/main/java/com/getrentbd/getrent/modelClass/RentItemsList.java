package com.getrentbd.getrent.modelClass;

public class RentItemsList {
    private String id;
    private String number;
    private String seat;
    private String num_floor;
    private String location;
    private String month;
    private String rent;
    private String catagory;
    private String timeDate;
    private String url;
    private String gender;
    private String address;
    private String description;
    private String name;
    private String email;

    public RentItemsList(String id, String number, String seat,
                         String num_floor, String location, String month,
                         String rent, String catagory, String timeDate, String url,
                         String gender, String address, String description, String name,
                         String email) {
        this.id = id;
        this.number = number;
        this.seat = seat;
        this.num_floor = num_floor;
        this.location = location;
        this.month = month;
        this.rent = rent;
        this.catagory = catagory;
        this.timeDate = timeDate;
        this.url = url;
        this.gender = gender;
        this.address = address;
        this.description = description;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getSeat() {
        return seat;
    }

    public String getNum_floor() {
        return num_floor;
    }

    public String getLocation() {
        return location;
    }

    public String getMonth() {
        return month;
    }

    public String getRent() {
        return rent;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public String getUrl() {
        return url;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
