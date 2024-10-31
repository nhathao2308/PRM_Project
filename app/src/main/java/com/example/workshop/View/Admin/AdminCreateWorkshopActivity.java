package com.example.workshop.View.Admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class AdminCreateWorkshopActivity extends AppCompatActivity {
    private EditText editTextWorkshopName, editTextLocation, editTextPrice;
    private TextView imageViewWorkshop;
    private Button buttonSelectStartDate, buttonSelectEndDate, buttonUploadImage, buttonCreateWorkshop;

    private WorkshopPresenter presenter;
    private Uri imageUri;
    private LocalDateTime startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workshop);

        editTextWorkshopName = findViewById(R.id.edit_text_name);
        editTextLocation = findViewById(R.id.edit_text_location);
        editTextPrice = findViewById(R.id.edit_text_price);
        imageViewWorkshop = findViewById(R.id.textViewImageUrl);
        buttonSelectStartDate = findViewById(R.id.buttonSelectStartDate);
        buttonSelectEndDate = findViewById(R.id.buttonSelectEndDate);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonCreateWorkshop = findViewById(R.id.button_create);

        presenter = new WorkshopPresenter(new IAdminView() {
            @Override
            public void onWorkshopCreatedSuccess() {
                Toast.makeText(AdminCreateWorkshopActivity.this, "Workshop created successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after success
            }

            @Override
            public void displayError(String message) {
                Toast.makeText(AdminCreateWorkshopActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWorkshopUpdatedSuccess() {

            }

            @Override
            public void displayWorkshops(List<Workshop> workshops) {
                // Not used in this context
            }

            @Override
            public void displayWorkshopDetails(Workshop workshop) {
                // Not used in this context
            }

            @Override
            public void onWorkshopUpdateSuccess() {

            }
        });

        buttonSelectStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        buttonSelectEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        buttonUploadImage.setOnClickListener(v -> openImagePicker());
        buttonCreateWorkshop.setOnClickListener(v -> createWorkshop());
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    // Show time picker after selecting the date
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
                        startTime = LocalDateTime.of(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH), hourOfDay, minute1);
                        buttonSelectStartDate.setText("Start Date: " + startTime.toString());
                    } else {
                        endTime = LocalDateTime.of(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH) + 1, selectedDate.get(Calendar.DAY_OF_MONTH), hourOfDay, minute1);
                        buttonSelectEndDate.setText("End Date: " + endTime.toString());
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void createWorkshop() {
        String name = editTextWorkshopName.getText().toString();
        String location = editTextLocation.getText().toString();
        int price = Integer.parseInt(editTextPrice.getText().toString());
        int capacity = 10; // Example capacity; you can change as needed
        int sold = 0; // Initially no tickets sold
        String description = "Workshop description"; // Add description logic as needed
        Date date = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp firestoreStartTime = new Timestamp(date);
        Date endDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
        Timestamp firestoreEndTime = new Timestamp(endDate);
        if (startTime != null && endTime != null) {
            // Create Workshop object
            CreateWorkshopModel newWorkshop = new CreateWorkshopModel(name, location, price, firestoreStartTime,firestoreEndTime, "", description, capacity, sold);
            uploadImageAndCreateWorkshop(newWorkshop);
        } else {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
        }
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
            imageViewWorkshop.setText(imageUri.toString()); // Display the selected image URI
        }
    }


    private void uploadImageAndCreateWorkshop(CreateWorkshopModel workshop) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");
        if(!Objects.equals(workshop.getImage(), "")){
            // Upload image
            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        workshop.setImage(uri.toString()); // Set image URL to workshop
                        presenter.createWorkshop(workshop); // Create workshop
                    })
            ).addOnFailureListener(e -> {
                Toast.makeText(AdminCreateWorkshopActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        else {
            workshop.setImage("gs://androiddemo-f116c.appspot.com/463325847_1094642215558308_921848514450405582_n.jpg");}
        presenter.createWorkshop(workshop);
    }
}
