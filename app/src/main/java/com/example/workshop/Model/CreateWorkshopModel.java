package com.example.workshop.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.Date;

public class CreateWorkshopModel {
    @PropertyName("Name")
    private String Name;

    @PropertyName("Location")
    private String Location;

    @PropertyName("Price")
    private int Price;

    @PropertyName("StartTime")
    private Timestamp StartTime;

    @PropertyName("EndTime")
    private Timestamp EndTime;

    @PropertyName("Image")
    private String Image;

    @PropertyName("Description")
    private String Description;

    @PropertyName("Capacity")
    private int Capacity;

    @PropertyName("Sold")
    private int Sold;


    public CreateWorkshopModel( String name, String location, int price, Timestamp startTime, Timestamp endTime,String imageUrl, String description, int capacity, int sold) {
        this.Image = imageUrl;
        this.Name = name;
        this.Location = location;
        this.Price = price;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Description = description;
        this.Capacity = capacity;
        this.Sold = sold;
    }

    public CreateWorkshopModel() {
    }


    @PropertyName("Name")
    public String getName() {
        return Name;
    }

    @PropertyName("Name")
    public void setName(String name) {
        Name = name;
    }

    @PropertyName("Location")
    public String getLocation() {
        return Location;
    }

    @PropertyName("Location")
    public void setLocation(String location) {
        Location = location;
    }

    @PropertyName("Price")
    public int getPrice() {
        return Price;
    }

    @PropertyName("Price")
    public void setPrice(int price) {
        Price = price;
    }

    @PropertyName("StartTime")
    public Timestamp getStartTime() {
        return StartTime;
    }

    @PropertyName("StartTime")
    public void setStartTime(Timestamp startTime) {
        StartTime = startTime;
    }

    @PropertyName("EndTime")
    public Timestamp getEndTime() {
        return EndTime;
    }

    @PropertyName("EndTime")
    public void setEndTime(Timestamp endTime) {
        EndTime = endTime;
    }

    @PropertyName("Image")
    public String getImage() {
        return Image;
    }

    @PropertyName("Image")
    public void setImage(String image) {
        Image = image;
    }

    @PropertyName("Description")
    public String getDescription() {
        return Description;
    }

    @PropertyName("Description")
    public void setDescription(String description) {
        Description = description;
    }

    @PropertyName("Capacity")
    public int getCapacity() {
        return Capacity;
    }

    @PropertyName("Capacity")
    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    @PropertyName("Sold")
    public int getSold() {
        return Sold;
    }

    @PropertyName("Sold")
    public void setSold(int sold) {
        Sold = sold;
    }
}
