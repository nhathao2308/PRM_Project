package com.example.workshop.View.Register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workshop.Presenter.Register.RegisterPresenter;
import com.example.workshop.View.Login.LoginActivity;
import com.example.workshop.databinding.RegisterBinding;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {
    private RegisterBinding binding;
    private RegisterPresenter registerPresenter;

    // Launcher for camera to capture face image
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        binding = RegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerPresenter = new RegisterPresenter(this);

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        captureFaceAndRegister(bitmap);
                    }
                }
        );

        binding.regBtnSignup.setOnClickListener(v -> openCamera());

        binding.buttonLoginBack.setOnClickListener(v -> registerPresenter.onLoginBackClicked());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private void captureFaceAndRegister(Bitmap bitmap) {
        if (bitmap == null) {
            showErrorMessage("No face image found. Please capture your face.");
            return; // Exit if no bitmap is available
        }

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        registerPresenter.onRegisterClicked(binding.regUsername.getText().toString(), binding.regPassword.getText().toString(), bitmap);
                    } else {
                        showErrorMessage("No face detected. Please try again.");
                    }
                })
                .addOnFailureListener(e -> {
                    showErrorMessage("Face detection failed: " + e.getMessage());
                });
    }

    public void onRegisterSuccess() {
        Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
