package com.example.workshop.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.workshop.Presenter.Login.LoginPresenter;
import com.example.workshop.Profile.IProfilePresenter;
import com.example.workshop.Profile.ProfilePresenter;
import com.example.workshop.R;
import com.example.workshop.View.Login.ILoginView;
import com.example.workshop.View.Login.LoginActivity;
import com.example.workshop.View.MainActivity;
import com.example.workshop.View.Register.RegisterActivity;
import com.example.workshop.databinding.FragmentProfileBinding;
import com.example.workshop.databinding.LoginBinding;

public class LogoutActivity extends AppCompatActivity implements IProfileView {
    private ProfilePresenter presenter;
    private FragmentProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profile);
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        presenter = new ProfilePresenter(this);

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                presenter.onLogoutClicked();
            }
        });

//        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = binding.inputUsername.getText().toString();
//                String password = binding.inputPassword.getText().toString();
//                presenter.onLoginClicked(email, password);
//            }
//        });
    }


    @Override
    public void LogoutSuccess() {
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showErrorMessage(String message) {

    }
}