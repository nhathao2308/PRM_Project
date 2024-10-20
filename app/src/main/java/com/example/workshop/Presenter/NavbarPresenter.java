package com.example.workshop.Presenter;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

import com.example.workshop.R;
import com.example.workshop.View.HomeFragment;
import com.example.workshop.View.MainActivity;
import com.example.workshop.View.ProfileFragment;
import com.example.workshop.View.TicketFragment;


public class NavbarPresenter implements INavabrPresenter {

    private final MainActivity mainActivity;

    public NavbarPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onNavigationItemSelected(int itemId) {
        Fragment fragment = null;
        if (itemId == R.id.miHome) {

            fragment = new HomeFragment();
        } else if (itemId == R.id.miProfile) {
            fragment = new ProfileFragment();

        } else if (itemId == R.id.miTicket) {
            fragment = new TicketFragment();

        } else {
            throw new IllegalStateException("Unexpected value: " + itemId);

        }


        if (fragment != null) {
            mainActivity.showFragment(fragment);  // Use mainActivity to call showFragment
        }
    }

}
