package com.example.securetext;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.securetext.Database.RoomDB;
import com.example.securetext.Model.Key;
import com.example.securetext.Utility.AuthenticatorTask;
import com.example.securetext.Utility.FingerPrintAuthenticator;
import com.example.securetext.fragments.AboutUsFragment;
import com.example.securetext.fragments.HomeFragment;
import com.example.securetext.fragments.MessageFragment;
import com.example.securetext.fragments.SecretChangeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    RelativeLayout relativeLayout;

    // Make a database instance
    RoomDB roomDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bnView);
        relativeLayout = findViewById(R.id.main);
        //relativeLayout.setVisibility(View.VISIBLE);

        // calling function to input random data at first and store in shared preference
        persistKey();

        try {
            AuthenticatorTask authenticatorTask = new AuthenticatorTask() {
                @Override
                public void afterValidationSuccess() {
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onValidationFailed() {
                    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                    if (keyguardManager.isKeyguardSecure()) {
                        ((Activity) MainActivity.this).finish();
                        System.exit(0);
                    } else {
                        Toast.makeText(MainActivity.this, "Phone doesn't contain password", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            List<Key> keyList = roomDB.keyDao().getAllKey();
            Boolean security = keyList.get(0).getSecurity();
            if (security) {
                FingerPrintAuthenticator fingerPrintAuthenticator = new FingerPrintAuthenticator(MainActivity.this, authenticatorTask, relativeLayout);
                fingerPrintAuthenticator.addAuthentication();
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.nav_home) {
                    loadFragment(new HomeFragment(), false);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_message) {
                    loadFragment(new MessageFragment(), false);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_changeKey) {
                    loadFragment(new SecretChangeFragment(), false);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_aboutUs) {
                    loadFragment(new AboutUsFragment(), false);
                    return true;
                } else {
                    Toast.makeText(MainActivity.this, "Default Clicked", Toast.LENGTH_SHORT).show();
                    return false;
                }
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


    private void persistKey() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            roomDB = RoomDB.getInstance(this);
            if (!sharedPreferences.getBoolean("firstTime", false)) {
                Key key = new Key();
                key.setKey("anfnlbfi{apmfoanf");
                key.setMessagebackup(true);
                key.setSecurity(true);

                roomDB.keyDao().saveItem(key);
                editor.putBoolean("firstTime", true);
                editor.commit();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            ((Activity) MainActivity.this).finish();
            System.exit(0);
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Tap back button again in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}