package com.example.workshop.Model;



import java.util.Date;

public class Workshop {
    private String id;             // Unique ID for the workshop
    private String name;           // Name of the workshop
    private String location;       // Location of the workshop
    private int price;             // Price of the workshop (in VND)
    private Date startTime;        // Start time of the workshop (Date object)
    private Date endTime;          // End time of the workshop (Date object)
    private String imageUrl;       // URL of the workshop image
    private String description;    // Description of the workshop
    private int capacity;          // Maximum capacity of participants
    private int sold;              // Number of sold tickets

    // Constructor


    public Workshop() {
    }

    public Workshop(String id, String name, String location, int price, Date startTime, Date endTime,
                    String imageUrl, String description, int capacity, int sold) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageUrl = imageUrl;
        this.description = description;
        this.capacity = capacity;
        this.sold = sold;
    }


    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSold() {
        return sold;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}

