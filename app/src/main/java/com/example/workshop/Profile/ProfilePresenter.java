package com.example.workshop.Profile;

import com.example.workshop.View.Login.ILoginView;
import com.example.workshop.View.Profile.IProfileView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilePresenter implements IProfilePresenter {

    private final IProfileView profileView;
    private final FirebaseAuth auth;

    public ProfilePresenter(IProfileView profileView) {
        this.profileView = profileView;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onLogoutClicked() {
        auth.signOut();
    }
}
