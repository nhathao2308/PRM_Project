package com.example.workshop.Presenter.Login;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.workshop.View.Login.ILoginView;
import com.example.workshop.View.Login.LoginActivity;
import com.example.workshop.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements ILoginPresenter {
    private final ILoginView loginView;
    private final FirebaseAuth auth;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        this.auth = FirebaseAuth.getInstance();
    }

    public void onLoginClicked(String email, String password) {
        if (email.equals("admin@gmail.com") && password.equals("12345")) {
            loginView.adminLogin(); }
        else {auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseAuth", "User login successful");
                            loginView.onLoginSuccess();
                        } else {
                            Log.e("FirebaseAuth", "Error logging in: ", task.getException());
                            loginView.showErrorMessage(task.getException() != null ? task.getException().getMessage() : "Login failed");
                        }
                    }
                });}

    }

    @Override
    public void onSignUpClicked() {
        loginView.navigateToRegister();
    }
}
