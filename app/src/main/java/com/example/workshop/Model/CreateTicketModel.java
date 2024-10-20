package com.example.workshop.Model;

public class CreateTicketModel {
    private int Status;          // Status of the ticket (1 could mean active, etc.)
    private String UserId;       // ID of the user who purchased the ticket
    private String WorkshopId;

    public CreateTicketModel(int status, String workshopId, String userId) {
        Status = status;
        WorkshopId = workshopId;
        UserId = userId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getWorkshopId() {
        return WorkshopId;
    }

    public void setWorkshopId(String workshopId) {
        WorkshopId = workshopId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
