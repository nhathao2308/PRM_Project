package com.example.workshop.Presenter.Register;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.workshop.View.Register.IRegisterView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class RegisterPresenter implements IRegisterPresenter {
    private final IRegisterView registerView;
    private final FirebaseAuth auth;
    private final FirebaseStorage storage;

    public RegisterPresenter(IRegisterView registerView) {
        this.registerView = registerView;
        this.auth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    public void onRegisterClicked(String email, String password, Bitmap faceBitmap) {
        createUser(email, password, faceBitmap);
    }

    private void createUser(String email, String password, Bitmap faceBitmap) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseAuth", "User registration successful");
                            saveFaceImage(faceBitmap, task.getResult().getUser().getUid());
                            registerView.onRegisterSuccess(); // Notify success
                        } else {
                            Log.e("FirebaseAuth", "Error creating user: ", task.getException());
                            registerView.showErrorMessage(task.getException() != null ? task.getException().getMessage() : "Registration failed");
                        }
                    }
                });
    }

    private void saveFaceImage(Bitmap faceBitmap, String userId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        faceBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference faceImageRef = storage.getReference().child("faceImages/" + userId + ".png");

        faceImageRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d("FirebaseStorage", "Face image uploaded successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseStorage", "Error uploading face image: ", e);
                });
    }

    @Override
    public void onRegisterClicked(String email, String password) {

    }

    @Override
    public void onLoginBackClicked() {
        registerView.navigateToLogin();
    }
}
