package com.example.workshop.View.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Workshop.WorkshopPresenter;
import com.example.workshop.R;
import com.example.workshop.View.Ticket.TicketFragment;
import com.google.firebase.FirebaseApp;

import java.util.List;

public class HomeFragment extends Fragment implements IHomeView {
    private RecyclerView workshopRecyclerView;
    private WorkshopAdapter workshopAdapter;
    private WorkshopPresenter presenter;
    private FirebaseApp app;
        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            FirebaseApp.initializeApp(this.getContext());

            View view = inflater.inflate(R.layout.fragment_home, container, false);

        workshopRecyclerView = view.findViewById(R.id.workshop_recycler_view);
        workshopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter = new WorkshopPresenter(this);
        presenter.fetchAllWorkshops();
        return view;
    }




    @Override
    public void displayWorkshops(List<Workshop> workshops) {
        workshopAdapter = new WorkshopAdapter(getContext(), workshops);
        workshopRecyclerView.setAdapter(workshopAdapter);
    }


    @Override
    public void displayError(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayWorkshopDetails(Workshop workshop) {

    }

    @Override
    public void onTicketPurchaseSuccess() {

    }
}