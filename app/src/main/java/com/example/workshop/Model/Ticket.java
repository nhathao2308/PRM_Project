package com.example.workshop.Model;

public class Ticket {
    private String id;           // Unique ID for the ticket
    private int status;          // Status of the ticket (1 could mean active, etc.)
    private String userId;       // ID of the user who purchased the ticket
    private String workshopId;   // ID of the associated workshop
    private Workshop workshop;

    // Constructor
    public Ticket(String id, int status, String userId, String workshopId, Workshop workshop) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.workshopId = workshopId;
        this.workshop = workshop;
    }

    public Ticket(int status, String userId, String workshopId) {
        this.status = status;
        this.userId = userId;
        this.workshopId = workshopId;
    }

    public Ticket(String userId, int status, String id, String workshopId) {
        this.userId = userId;
        this.status = status;
        this.id = id;
        this.workshopId = workshopId;
    }

    // Getters
    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }
}

