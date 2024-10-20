package com.example.workshop.Presenter.Register;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.workshop.View.Register.IRegisterView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.regex.Pattern;

public class RegisterPresenter implements IRegisterPresenter {
    private final IRegisterView registerView;
    private final FirebaseAuth auth;

    public RegisterPresenter(IRegisterView registerView) {
        this.registerView = registerView;
        this.auth = FirebaseAuth.getInstance();

        // Initialize Firebase App Check with the provider you want (SafetyNet or PlayIntegrity)
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance() // Use this for Play Integrity
                // SafetyNetAppCheckProviderFactory.getInstance()  // Uncomment this if you're using SafetyNet
        );
    }

    public void onRegisterClicked(String email, String password) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Validate email and password
        if (email.isEmpty() || password.isEmpty()) {
            registerView.showErrorMessage("Email or password cannot be empty");
            return;
        }

        if (!pattern.matcher(email).matches()) {
            registerView.showErrorMessage("Email is not valid");
            return;
        }
        if (password.length() < 8) {
            registerView.showErrorMessage("Password must be at least 8 characters");
            return;
        }

        // At this point, Firebase App Check will automatically verify the device using reCAPTCHA or Play Integrity
        // We now proceed with creating the user
        createUser(email, password);
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseAuth", "User registration successful");
                            registerView.onRegisterSuccess(); // Notify success
                        } else {
                            Log.e("FirebaseAuth", "Error creating user: ", task.getException());
                            registerView.showErrorMessage(task.getException() != null ? task.getException().getMessage() : "Registration failed");
                        }
                    }
                });
    }
}
