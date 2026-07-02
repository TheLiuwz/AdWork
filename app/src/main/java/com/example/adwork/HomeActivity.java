package com.example.adwork;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private ShiWuFragment ShiWuFragment;
    private GeRenFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ShiWuFragment = new ShiWuFragment();
        profileFragment = new GeRenFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, profileFragment, "profile")
                .hide(profileFragment)
                .add(R.id.fragment_container, ShiWuFragment, "transaction")
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (item.getItemId() == R.id.nav_transaction) {
                ft.show(ShiWuFragment).hide(profileFragment);
            } else if (item.getItemId() == R.id.nav_profile) {
                ft.show(profileFragment).hide(ShiWuFragment);
            }
            ft.commit();
            return true;
        });
    }
}
