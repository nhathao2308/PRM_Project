package com.example.workshop.View.Ticket;

import com.example.workshop.Model.Ticket;

import java.util.List;

public interface ITicketView {

    void displayTickets(List<Ticket> tickets);
    void displayError(String message);
}
