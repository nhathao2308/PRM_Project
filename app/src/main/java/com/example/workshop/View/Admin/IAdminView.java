package com.example.workshop.View.Admin;

import com.example.workshop.Model.Workshop;

import java.util.List;

public interface IAdminView {
    void onWorkshopUpdatedSuccess();

    void displayWorkshops(List<Workshop> workshops);

    void onWorkshopCreatedSuccess();

    void displayError(String message);

    void displayWorkshopDetails(Workshop workshop);
    void  onWorkshopUpdateSuccess();
}
