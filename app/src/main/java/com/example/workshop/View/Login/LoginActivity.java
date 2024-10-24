package com.example.workshop.View.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.workshop.Presenter.Login.LoginPresenter;
import com.example.workshop.R;
import com.example.workshop.View.MainActivity;
import com.example.workshop.View.Register.RegisterActivity;
import com.example.workshop.databinding.LoginBinding;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private LoginPresenter presenter;
    private LoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        presenter = new LoginPresenter(this);
        String mail = binding.inputUsername.getText().toString();
        String pwd = binding.inputPassword.getText().toString();
        binding.buttonSignup.setOnClickListener(v -> presenter.onSignUpClicked());
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.inputUsername.getText().toString();
                String password = binding.inputPassword.getText().toString();
                presenter.onLoginClicked(email, password);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginSuccess() {        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}