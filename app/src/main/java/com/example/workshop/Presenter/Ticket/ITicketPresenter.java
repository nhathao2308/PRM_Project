package com.example.workshop.Presenter.Ticket;

public interface ITicketPresenter {
    void loadTickets();
    void fetchWorkshop(String workshopId, String ticketId, int status, String userId, int totalWorkshops);
}
