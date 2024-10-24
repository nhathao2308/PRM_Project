package com.example.workshop.View.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Workshop.WorkshopPresenter;
import com.example.workshop.R;
import com.example.workshop.View.MainActivity;
import com.example.workshop.View.Ticket.TicketFragment;
import com.example.workshop.databinding.WorkshopDetailBinding;

import java.util.List;

public class WorkshopDetailActivity extends AppCompatActivity implements  IHomeView {
    private WorkshopPresenter presenter;
    private WorkshopDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View Binding
        binding = WorkshopDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize presenter
        presenter = new WorkshopPresenter(this);

        // Get workshop ID from intent
        Intent intent = getIntent();
        String workshopId = intent.getStringExtra("workshop_id");
        if (workshopId != null) {
            presenter.getWorkshopById(workshopId); // Fetch workshop details
        }
        binding.numberPickerTickets.setMinValue(1);
        binding.numberPickerTickets.setMaxValue(10);
        binding.numberPickerTickets.setValue(1);
        binding.numberPickerTickets.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal < 1 || newVal > 10) {
                Toast.makeText(WorkshopDetailActivity.this, "Number of tickets must be greater than 0 and less than or equal to 10.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonBuy.setOnClickListener(v -> {
            // Get the number of tickets from the NumberPicker
            int numOfTickets = binding.numberPickerTickets.getValue();

            // Calculate total price
            double ticketPrice = Double.parseDouble(binding.txtPrice.getText().toString());
            double totalPrice = ticketPrice * numOfTickets;

            // Start PaymentActivity and pass the workshop ID and total price
            Intent paymentIntent = new Intent(this, PaymentActivity.class);
            paymentIntent.putExtra("workshop_id", workshopId);
            paymentIntent.putExtra("number_of_tickets", numOfTickets);
            paymentIntent.putExtra("total_price", totalPrice); // Pass total price
            startActivity(paymentIntent);
        });

    }

    @Override
    public void displayWorkshops(List<Workshop> workshops) {

    }

    @Override
    public void displayError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void displayWorkshopDetails(Workshop workshop) {
        if (workshop != null) {
            binding.workshopName.setText(workshop.getName());
            binding.txtLocation.setText(workshop.getLocation());
            binding.txtStartTime.setText(workshop.getStartTime() != null ? workshop.getStartTime().toString() : "Not Available");
            binding.txtEndTime.setText(String.valueOf(workshop.getPrice()));
            binding.txtDescription.setText(workshop.getDescription());
            Glide.with(this)
                    .load(workshop.getImageUrl()) // Load the image URL from the Workshop object
                    .error(R.drawable.wslist_1) // Optional error imag
                    .into(binding.workshopImage); // Set the ImageView to display the image
        }
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