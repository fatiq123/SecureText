package com.example.securetext;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.securetext.fragments.AboutUsFragment;
import com.example.securetext.fragments.HomeFragment;
import com.example.securetext.fragments.MessageFragment;
import com.example.securetext.fragments.SecretChangeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private static final int NAV_HOME = R.id.nav_home;
    private static final int NAV_MESSAGE = R.id.nav_message;
    private static final int NAV_CHANGE_KEY = R.id.nav_changeKey;
    private static final int NAV_ABOUT_US = R.id.nav_aboutUs;

    BottomNavigationView bottomNavigationView;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bnView);
        relativeLayout = findViewById(R.id.main);
        relativeLayout.setVisibility(View.VISIBLE);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case NAV_HOME:
                        loadFragment(new HomeFragment(), false);
                        return true;

                    case NAV_MESSAGE:
                        loadFragment(new MessageFragment(), false);
                        return true;

                    case NAV_CHANGE_KEY:
                        loadFragment(new SecretChangeFragment(), false);
                        return false;

                    case NAV_ABOUT_US:
                        loadFragment(new AboutUsFragment(), false);
                        return false;

                    default:
                        Toast.makeText(MainActivity.this, "Default Clicked", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

    }

    private void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (flag) fragmentTransaction.add(R.id.container, fragment);
        else fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}