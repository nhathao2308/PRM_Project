package com.example.workshop.Presenter.Ticket;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.workshop.Model.Ticket;
import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Ticket.ITicketPresenter;
import com.example.workshop.View.Ticket.ITicketView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class TicketPresenter implements ITicketPresenter {

    private ITicketView ticketView;
    private List<Ticket> ticketList;
    private FirebaseFirestore db;
    private String currentUserId;  // Current logged-in user ID

    public TicketPresenter(ITicketView ticketView) {
        this.ticketView = ticketView;
        this.db = FirebaseFirestore.getInstance();  // Initialize Firestore
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.ticketList = new ArrayList<>();
    }

    @Override
    public void loadTickets() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            ticketView.displayError("User not logged in.");
            return; // Exit if user is not authenticated
        }

        currentUserId = user.getUid();
        CollectionReference ticketRef = db.collection("Ticket");

        ticketRef.whereEqualTo("UserId", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ticketList.clear(); // Clear old data
                            if (task.getResult().isEmpty()) {
                                ticketView.displayTickets(new ArrayList<>()); // Notify no tickets
                                return;
                            }

                            // Keep track of how many workshops need to be fetched
                            int totalWorkshops = task.getResult().size();
                            if (totalWorkshops == 0) {
                                ticketView.displayTickets(new ArrayList<>());
                                return;
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get ticket details
                                String ticketId = document.getId();
                                int status = document.getLong("Status").intValue();
                                String userId = document.getString("UserId");
                                String workshopId = document.getString("WorkshopId");

                                // Fetch the workshop associated with this ticket
                                fetchWorkshop(workshopId, ticketId, status, userId, totalWorkshops);
                            }
                        } else {
                            Log.e("TicketPresenter", "Error getting tickets: ", task.getException());
                            ticketView.displayError("Failed to load tickets: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void fetchWorkshop(String workshopId, String ticketId, int status, String userId, int totalWorkshops) {
        // Get the workshop data from "workshops" collection based on workshopId
        DocumentReference workshopRef = db.collection("Workshop").document(workshopId);
        workshopRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot workshopSnapshot = task.getResult();
                    if (workshopSnapshot.exists()) {
                        // Create Workshop object from Firestore data
                        Workshop workshop = new Workshop(
                                workshopSnapshot.getId(),
                                workshopSnapshot.getString("Name"),
                                workshopSnapshot.getString("Location"),
                                workshopSnapshot.getLong("Price").intValue(),
                                workshopSnapshot.getDate("StartTime"),
                                workshopSnapshot.getDate("EndTime"),
                                workshopSnapshot.getString("ImageUrl"),
                                workshopSnapshot.getString("Description"),
                                workshopSnapshot.getLong("Capacity").intValue(),
                                workshopSnapshot.getLong("Sold").intValue()
                        );

                        // Add the ticket with the fetched workshop data
                        Ticket ticket = new Ticket(ticketId, status, userId, workshopId, workshop);
                        ticketList.add(ticket);

                        // Check if we have fetched all workshops
                        if (ticketList.size() == totalWorkshops) {
                            ticketView.displayTickets(ticketList);
                        }
                    } else {
                        ticketView.displayError("Workshop not found for ticket: " + ticketId);
                    }
                } else {
                    ticketView.displayError("Failed to load workshop for ticket: " + ticketId);
                }
            }
        });
    }
}
