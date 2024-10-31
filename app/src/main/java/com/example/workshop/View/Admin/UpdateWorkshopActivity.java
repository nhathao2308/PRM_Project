package com.example.workshop.View.Admin;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workshop.Model.CreateWorkshopModel;
import com.example.workshop.Model.Workshop;
import com.example.workshop.Presenter.Workshop.WorkshopPresenter;
import com.example.workshop.R;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UpdateWorkshopActivity extends AppCompatActivity {
    private EditText editTextWorkshopName, editTextLocation, editTextPrice, editTextDescription, editTextCapacity;
    private TextView textViewImageUrl;
    private Button buttonSelectStartDate, buttonSelectEndDate, buttonUploadImage, buttonUpdateWorkshop;

    private WorkshopPresenter presenter;
    private Uri imageUri;
    private LocalDateTime startTime, endTime;

    private String workshopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_workshop); // Make sure this matches your XML filename

        // Initialize UI components
        editTextWorkshopName = findViewById(R.id.edit_text_name);
        editTextLocation = findViewById(R.id.edit_text_location);
        editTextPrice = findViewById(R.id.edit_text_price);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextCapacity = findViewById(R.id.edit_text_capacity);
        textViewImageUrl = findViewById(R.id.textViewImageUrl);
        buttonSelectStartDate = findViewById(R.id.buttonSelectStartDate);
        buttonSelectEndDate = findViewById(R.id.buttonSelectEndDate);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonUpdateWorkshop = findViewById(R.id.button_create);

        presenter = new WorkshopPresenter(new IAdminView() {
            @Override
            public void onWorkshopUpdatedSuccess() {
                Toast.makeText(UpdateWorkshopActivity.this, "Workshop updated successfully!", Toast.LENGTH_SHORT).show();

                // Navigate back to AdminActivity
                Intent intent = new Intent(UpdateWorkshopActivity.this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
                startActivity(intent);
                finish();
            }

            @Override
            public void displayError(String message) {
                Toast.makeText(UpdateWorkshopActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void displayWorkshops(List<Workshop> workshops) {}

            @Override
            public void onWorkshopCreatedSuccess() {}

            @Override
            public void displayWorkshopDetails(Workshop workshop) {}

            @Override
            public void onWorkshopUpdateSuccess() {

            }
        });

        // Get workshop data from Intent for update
        Intent intent = getIntent();
        workshopId = intent.getStringExtra("workshopId");
        if (workshopId != null) {
            String workshopName = intent.getStringExtra("workshopName");
            String workshopLocation = intent.getStringExtra("workshopLocation");
            int workshopPrice = intent.getIntExtra("workshopPrice", 0);
            String workshopImage = intent.getStringExtra("workshopImage");
            String workshopDescription = intent.getStringExtra("workshopDescription");
            int workshopCapacity = intent.getIntExtra("workshopCapacity", 0);


            // Retrieve and convert Date objects from intent
            Date workshopStartTimeDate = (Date) intent.getSerializableExtra("workshopStartTime");
            Date workshopEndTimeDate = (Date) intent.getSerializableExtra("workshopEndTime");

// Populate fields with existing workshop data
            editTextWorkshopName.setText(workshopName);
            editTextLocation.setText(workshopLocation);
            editTextPrice.setText(String.valueOf(workshopPrice));
            textViewImageUrl.setText(workshopImage);
            editTextDescription.setText(workshopDescription);
            editTextCapacity.setText(String.valueOf(workshopCapacity));

// Set start and end time if available
            if (workshopStartTimeDate != null) {
                startTime = workshopStartTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                buttonSelectStartDate.setText("Start Date: " + startTime.toString());
            }
            if (workshopEndTimeDate != null) {
                endTime = workshopEndTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                buttonSelectEndDate.setText("End Date: " + endTime.toString());
            }
        }

        // Set onClickListeners
        buttonSelectStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        buttonSelectEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        buttonUploadImage.setOnClickListener(v -> openImagePicker());
        buttonUpdateWorkshop.setOnClickListener(v -> updateWorkshop());
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    showTimePickerDialog(selectedDate, isStartDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog(Calendar selectedDate, boolean isStartDate) {
        int hour = selectedDate.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDate.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDate.set(Calendar.MINUTE, minute1);

                    if (isStartDate) {
                        startTime = LocalDateTime.of(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1,
                                selectedDate.get(Calendar.DAY_OF_MONTH), hourOfDay, minute1);
                        buttonSelectStartDate.setText("Start Date: " + startTime.toString());
                    } else {
                        endTime = LocalDateTime.of(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1,
                                selectedDate.get(Calendar.DAY_OF_MONTH), hourOfDay, minute1);
                        buttonSelectEndDate.setText("End Date: " + endTime.toString());
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void updateWorkshop() {
        String name = editTextWorkshopName.getText().toString();
        String location = editTextLocation.getText().toString();
        String priceString = editTextPrice.getText().toString();
        String capacityString = editTextCapacity.getText().toString();
        String description = editTextDescription.getText().toString();

        if (startTime == null || endTime == null) {
            Toast.makeText(this, "Please select start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || location.isEmpty() || priceString.isEmpty() || description.isEmpty() || capacityString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceString);
        int capacity = Integer.parseInt(capacityString);

        CreateWorkshopModel updatedWorkshop = new CreateWorkshopModel(
                name,
                location,
                price,
                new Timestamp(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant())),
                new Timestamp(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant())),
                "", // Image URL (to be updated)
                description,
                capacity,
                0 // Initially no tickets sold
        );

            uploadImageAndUpdateWorkshop(updatedWorkshop);

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            textViewImageUrl.setText(imageUri.toString()); // Display the selected image URI
        }
    }

    private void uploadImageAndUpdateWorkshop(CreateWorkshopModel workshop) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");
        if(!Objects.equals(workshop.getImage(), "")) {
            // Upload image
            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        workshop.setImage(uri.toString()); // Set image URL to workshop
                        presenter.updateWorkshop(workshopId, workshop); // Call the update method in the presenter
                    })
            ).addOnFailureListener(e -> {
                Toast.makeText(UpdateWorkshopActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        else {
            workshop.setImage("gs://androiddemo-f116c.appspot.com/463325847_1094642215558308_921848514450405582_n.jpg");}
        presenter.updateWorkshop(workshopId, workshop); // Call the update method in the presenter
        }
    }
