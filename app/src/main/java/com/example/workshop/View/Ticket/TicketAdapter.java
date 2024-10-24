package com.example.workshop.View.Ticket;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Model.Ticket;
import com.example.workshop.Model.Workshop;
import com.example.workshop.databinding.TicketCardBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> ticketList;
    private Context context;

    public TicketAdapter(Context context,List<Ticket> ticketList) {
        this.ticketList = ticketList;
        this.context = context;
    }

    public void updateTickets(List<Ticket> newTickets) {
        this.ticketList = newTickets;
        notifyDataSetChanged();  // Notify RecyclerView that data has changed
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TicketCardBinding binding = TicketCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TicketCardBinding binding;

        public TicketViewHolder(@NonNull TicketCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ticket ticket) {
            Workshop workshop = ticket.getWorkshop();
            binding.tkWorkshopName.setText(workshop.getName());
            binding.tkWorkshopId.setText(ticket.getId()); // Use ticket ID
            binding.tkLocation.setText(workshop.getLocation());

            // Format and set the start time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss", Locale.getDefault());
            String formattedStartTime = dateFormat.format(workshop.getStartTime());
            binding.tkStartTime.setText(formattedStartTime);

            binding.tkPrice.setText(String.format(Locale.getDefault(), "%d VND", workshop.getPrice()));

            // Update the status view
            updateStatusView(ticket);
        }

        private void updateStatusView(Ticket ticket) {
            String statusText;
            int statusColor;

            if (ticket.getStatus() == 1) {
                statusText = "Processing";
                statusColor = Color.GREEN;
            } else {
                statusText = "Used";
                statusColor = Color.RED;
            }

            binding.tkStatus.setText(statusText);
            binding.tkStatus.setTextColor(statusColor);
        }
    }
}