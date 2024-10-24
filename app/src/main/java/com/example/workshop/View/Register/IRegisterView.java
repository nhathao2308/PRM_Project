package com.example.workshop.View.Register;

import android.app.Activity;

public interface IRegisterView {
    void onRegisterSuccess();

    void showErrorMessage(String message);
    Activity getActivity();

    void navigateToLogin();
}
