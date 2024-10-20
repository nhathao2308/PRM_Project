package com.example.workshop.Presenter.Login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.workshop.View.Login.ILoginView;
import com.example.workshop.View.Login.LoginActivity;
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

    @Override
    public void onSignUpClicked() {
        loginView.navigateToRegister();
    }
    public void onLoginClicked(String email, String password) {
        // Validate email and password
        if (email.isEmpty() || password.isEmpty()) {
            loginView.showErrorMessage("Email or password cannot be empty");
            return;
        }

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseAuth", "Login successful");
                            loginView.onLoginSuccess(); // Notify the view on success
                        } else {
                            Log.e("FirebaseAuth", "Error logging in: ", task.getException());
                            loginView.showErrorMessage(task.getException() != null ? task.getException().getMessage() : "Login failed");
                        }
                    }


                });
    }
}
