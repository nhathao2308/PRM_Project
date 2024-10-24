package com.example.workshop.Presenter.Workshop;

public interface IWorkshopPresenter {
    void fetchAllWorkshops();
    void getWorkshopById(String workshopId);
    void createTicket(String workshopId, int numOfTicket);

}
