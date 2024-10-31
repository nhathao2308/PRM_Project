package com.example.workshop.View.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Workshop.WorkshopPresenter;
import com.example.workshop.R;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements  IAdminView {

    private RecyclerView recyclerView;
    private WorkshopAdapter workshopAdapter;
    private WorkshopPresenter workshopPresenter;
    private List<Workshop> workshopList;
    private Button buttonCreateWorkshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Ensure this matches your layout file name

        // Initialize views
        recyclerView = findViewById(R.id.admin_workshop_recycler_view);
        buttonCreateWorkshop = findViewById(R.id.create_ws_btn);

        // Set up RecyclerView
        workshopList = new ArrayList<>();
        workshopAdapter = new WorkshopAdapter(this, workshopList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(workshopAdapter);

        // Initialize presenter
        workshopPresenter = new WorkshopPresenter(this);
        workshopPresenter.fetchAllWorkshops();

        // Set up create workshop button
        buttonCreateWorkshop.setOnClickListener(v -> {
            // Navigate to the create workshop activity
            startActivity(new Intent(AdminActivity.this, AdminCreateWorkshopActivity.class));
        });
    }

    protected void onResume() {
        super.onResume();
        workshopPresenter.fetchAllWorkshops();
    }

    @Override
    public void onWorkshopUpdatedSuccess() {

    }

    @Override
    public void displayWorkshops(List<Workshop> workshops) {
        // Update the existing list and notify the adapter
        workshopList.clear();
        workshopList.addAll(workshops);
        workshopAdapter.notifyDataSetChanged();
    }

//
//    @Override
//    public void displayWorkshops(List<Workshop> workshops) {
//        workshopAdapter = new com.example.workshop.View.Admin.WorkshopAdapter(this, workshops);
//        recyclerView.setAdapter(workshopAdapter);
//    }

    @Override
    public void displayWorkshopDetails(Workshop workshop) {
        // Handle displaying workshop details if needed
    }

    @Override
    public void onWorkshopUpdateSuccess() {
    workshopPresenter.fetchAllWorkshops();
    }

    @Override
    public void displayError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWorkshopCreatedSuccess() {
        // Optionally handle workshop creation success
        Toast.makeText(this, "Workshop created successfully!", Toast.LENGTH_SHORT).show();
        workshopPresenter.fetchAllWorkshops(); // Refresh the workshop list
    }
}