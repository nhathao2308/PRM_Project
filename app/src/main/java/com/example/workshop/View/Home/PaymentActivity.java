package com.example.workshop.View.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Workshop.WorkshopPresenter;
import com.example.workshop.View.MainActivity;
import com.example.workshop.databinding.PaymentBinding;

import java.util.List;

public class PaymentActivity extends AppCompatActivity implements  IHomeView {
    private WorkshopPresenter presenter;
    private PaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new WorkshopPresenter(this);
        double totalPrice = getIntent().getDoubleExtra("total_price", 0); // Retrieve total price

        // Set the total price to the payment_total TextView
        binding.paymentTotal.setText(String.valueOf(totalPrice));
// Retrieve workshop ID and number of tickets from Intent extras
        String workshopId = getIntent().getStringExtra("workshop_id");
        int numOfTickets = getIntent().getIntExtra("number_of_tickets", 1); // Default to 1 if not provided

        // Set an OnClickListener on the payment button
        binding.paymentBtnpay.setOnClickListener(v -> {
            // Call the createTicket method from the presenter
            presenter.createTicket(workshopId, numOfTickets);
        });

        ;
    }

    @Override
    public void displayWorkshops(List<Workshop> workshops) {

    }

    @Override
    public void displayError(String message) {

    }

    @Override
    public void displayWorkshopDetails(Workshop workshop) {

    }

    @Override
    public void onTicketPurchaseSuccess() {
        // Start MainActivity and signal to show the TicketFragment
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("show_ticket_fragment", true); // Signal to show TicketFragment
        startActivity(intent);
        finish(); // Optionally close WorkshopDetailActivity

    }
}