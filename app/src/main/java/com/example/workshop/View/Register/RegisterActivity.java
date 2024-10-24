package com.example.workshop.View.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workshop.Presenter.Register.RegisterPresenter;
import com.example.workshop.View.Login.LoginActivity;
import com.example.workshop.databinding.RegisterBinding;
import com.google.firebase.FirebaseApp;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {
    private RegisterBinding binding;
    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        EdgeToEdge.enable(this);
        binding = RegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerPresenter = new RegisterPresenter(this);

        binding.regBtnSignup.setOnClickListener(v -> {
            String email = binding.regUsername.getText().toString();
            String password = binding.regPassword.getText().toString();
            registerPresenter.onRegisterClicked(email, password);
        });

        binding.buttonLoginBack.setOnClickListener(v -> registerPresenter.onLoginBackClicked());
    }

    public void onRegisterSuccess() {
        Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: finish RegisterActivity so it cannot be navigated back to
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public Activity getActivity() {
        return this; // Return the current activity
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
