package com.example.workshop.View.Ticket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workshop.Model.Ticket;
import com.example.workshop.Presenter.Ticket.ITicketPresenter;
import com.example.workshop.Presenter.Ticket.TicketPresenter;
import com.example.workshop.databinding.FragmentTicketBinding;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class TicketFragment extends Fragment implements ITicketView {

    private FragmentTicketBinding binding;
    private TicketAdapter ticketAdapter;
    private ITicketPresenter ticketPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getContext());

        binding = FragmentTicketBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize presenter
        ticketPresenter = new TicketPresenter(this);

        // Set up RecyclerView with an empty list initially
        ticketAdapter = new TicketAdapter(new ArrayList<>());
        binding.ticketRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ticketRecyclerView.setAdapter(ticketAdapter);

        // Load tickets from Firestore
        ticketPresenter.loadTickets();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding to avoid memory leaks
    }

    @Override
    public void displayTickets(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            // Show "no tickets" message if the list is empty
            binding.ticketRecyclerView.setVisibility(View.GONE);
            binding.noTicket.setVisibility(View.VISIBLE);
            binding.noTicket.setText("You have no tickets");
        } else {
            // Update adapter data and notify the RecyclerView
            binding.noTicket.setVisibility(View.GONE);
            binding.ticketRecyclerView.setVisibility(View.VISIBLE);

            // Update adapter data
            ticketAdapter = new TicketAdapter(tickets);
            binding.ticketRecyclerView.setAdapter(ticketAdapter);
            ticketAdapter.notifyDataSetChanged();  // Notify that data has changed
        }
    }

    @Override
    public void displayError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
