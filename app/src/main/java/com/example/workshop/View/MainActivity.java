package com.example.workshop.View;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.workshop.Presenter.NavbarPresenter;
import com.example.workshop.R;
import com.example.workshop.View.Ticket.TicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity implements INavbarView {
    private NavbarPresenter navbarPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        navbarPresenter = new NavbarPresenter(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            navbarPresenter.onNavigationItemSelected(item.getItemId());
            return true;
        });

        if (savedInstanceState == null) { // Only do this if no previous instance exists
            if (getIntent() != null && getIntent().getBooleanExtra("show_ticket_fragment", false)) {
                showFragment(new TicketFragment());
            } else {
                navbarPresenter.onNavigationItemSelected(R.id.miHome);
            }
        }
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
