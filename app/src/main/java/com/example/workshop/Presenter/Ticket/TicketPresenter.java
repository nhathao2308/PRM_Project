package com.example.workshop.Presenter.Ticket;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workshop.Model.Ticket;
import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Ticket.ITicketPresenter;
import com.example.workshop.View.Ticket.ITicketView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

        // Set up a listener to listen for changes in the Ticket collection
        ticketRef.whereEqualTo("UserId", currentUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TicketPresenter", "Listen failed.", e);
                            ticketView.displayError("Failed to load tickets: " + e.getMessage());
                            return;
                        }

                        Log.d("TicketPresenter", "Listener triggered. Snapshot size: " + snapshots.size());

                        ticketList.clear(); // Clear old data
                        if (snapshots == null || snapshots.isEmpty()) {
                            ticketView.displayTickets(new ArrayList<>()); // Notify no tickets
                            return;
                        }

                        // Process each document in the snapshots
                        int totalWorkshops = snapshots.size();
                        for (QueryDocumentSnapshot document : snapshots) {
                            // Get ticket details
                            String ticketId = document.getId();
                            int status = document.getLong("Status").intValue();
                            String userId = document.getString("UserId");
                            String workshopId = document.getString("WorkshopId");

                            // Log ticket information
                            Log.d("TicketPresenter", "Fetched Ticket: ID=" + ticketId + ", UserId=" + userId + ", WorkshopId=" + workshopId);

                            // Fetch the workshop associated with this ticket
                            fetchWorkshop(workshopId, ticketId, status, userId, totalWorkshops);
                        }
                    }
                });
    }

    public void fetchWorkshop(String workshopId, String ticketId, int status, String userId, int totalWorkshops) {
        // Get the workshop data from the "workshops" collection based on workshopId
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