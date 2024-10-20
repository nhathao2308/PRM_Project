package com.example.workshop.View.Home;

import com.example.workshop.Model.Workshop;

import java.util.List;

public interface IHomeView {
    void displayWorkshops(List<Workshop> workshops);
    void displayError(String message); // New method for error handling
    void displayWorkshopDetails(Workshop workshop); // New method for displaying workshop details

}
