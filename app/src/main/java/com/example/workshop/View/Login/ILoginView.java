package com.example.workshop.View.Login;

public interface ILoginView {
    void navigateToRegister();
    void onLoginSuccess();
    void showErrorMessage(String message);

}
