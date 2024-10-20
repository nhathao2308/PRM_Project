    package com.example.workshop.Presenter.Workshop;

    import android.net.Uri;

    import androidx.annotation.NonNull;

    import com.example.workshop.Model.CreateTicketModel;
    import com.example.workshop.Model.Ticket;
    import com.example.workshop.Model.Workshop;
    import com.example.workshop.View.Home.IHomeView;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.Timestamp;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.CollectionReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QuerySnapshot;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    public class WorkshopPresenter implements  IWorkshopPresenter {
        private IHomeView homeView;
        private List<Workshop> workshopList;
        private FirebaseFirestore db;
        FirebaseAuth auth = FirebaseAuth.getInstance();
       FirebaseUser currentUser = auth.getCurrentUser();


        public WorkshopPresenter(IHomeView homeViewview) {
            this.homeView = homeViewview;
            this.workshopList = new ArrayList<>();
            this.db = FirebaseFirestore.getInstance();
        }


        @Override
        public void fetchAllWorkshops() {
            CollectionReference workshopsRef = db.collection("Workshop"); // Your Firestore collection

            workshopsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        workshopList.clear(); // Clear the list to avoid duplication
                        for (DocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("Name");
                            String location = document.getString("Location");
                            int price = document.getLong("Price").intValue();
                            Timestamp startTimeTimestamp = document.getTimestamp("StartTime");
                            Timestamp endTimeTimestamp = document.getTimestamp("EndTime");
                            Date startTime = startTimeTimestamp != null ? startTimeTimestamp.toDate() : null;
                            Date endTime = endTimeTimestamp != null ? endTimeTimestamp.toDate() : null;
                            String imageUrl = document.getString("Image");
                            String description = document.getString("Description");
                            int capacity = document.getLong("Capacity").intValue();
                            int sold = document.getLong("Sold").intValue();

                            // Create a new Workshop object with mapped values
                            Workshop workshop = new Workshop(id, name, location, price, startTime, endTime, imageUrl, description, capacity, sold);// Map Firestore doc to Workshop

                            // Get the download URL for the image
                            assert workshop != null;
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(workshop.getImageUrl());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    workshop.setImageUrl(uri.toString()); // Update the Workshop object with the download URL
                                    workshopList.add(workshop); // Add workshop to the list

                                    // Check if this is the last workshop to add
                                    if (workshopList.size() == task.getResult().size()) {
                                        homeView.displayWorkshops(workshopList); // Update the view
                                        homeView.displayError("Fetch Successfully");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    homeView.displayError(exception.getMessage());
                                }
                            });
                        }
                    } else {
                        // Handle the error
                        homeView.displayError(task.getException().getMessage());
                    }
                }
            });
        }


        @Override
        public void getWorkshopById(String workshopId) {
            db.collection("Workshop").document(workshopId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String id = document.getId();
                            String name = document.getString("Name");
                            String location = document.getString("Location");
                            int price = document.getLong("Price").intValue();
                            Timestamp startTimeTimestamp = document.getTimestamp("StartTime");
                            Timestamp endTimeTimestamp = document.getTimestamp("EndTime");
                            Date startTime = startTimeTimestamp != null ? startTimeTimestamp.toDate() : null;
                            Date endTime = endTimeTimestamp != null ? endTimeTimestamp.toDate() : null;
                            String imageUrl = document.getString("Image");
                            String description = document.getString("Description");
                            int capacity = document.getLong("Capacity").intValue();
                            int sold = document.getLong("Sold").intValue();

                            // Create a new Workshop object with mapped values
                            Workshop workshop = new Workshop(id, name, location, price, startTime, endTime, imageUrl, description, capacity, sold);// Map Firestore doc to Workshop

                            // Get the download URL for the image
                            assert workshop != null;
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(workshop.getImageUrl());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    workshop.setImageUrl(uri.toString()); // Update the Workshop object with the download URL
                                    homeView.displayWorkshopDetails(workshop);  // Display details in the view


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    homeView.displayError(exception.getMessage());
                                }
                            });
                        } else {
                            homeView.displayError("Workshop not found");
                        }
                    } else {
                        homeView.displayError(task.getException().getMessage());
                    }
                }
            });
        }
        public void createTicket(String workshopId) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                homeView.displayError("User not logged in.");
                return;
            }

           String userId = user.getUid();
            CreateTicketModel newTicket = new CreateTicketModel(1, userId, workshopId); // Create a new ticket

            db.collection("Ticket") // Your Firestore collection for tickets
                    .add(newTicket)
                    .addOnSuccessListener(documentReference -> {
                        homeView.onTicketPurchaseSuccess(); // Notify the view
                    })
                    .addOnFailureListener(e -> {
                        homeView.displayError("Failed to purchase ticket: " + e.getMessage()); // Handle failure
                    });
        }
    }
