package com.example.dhor.billigbytur.Activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.dhor.billigbytur.Fragments.HomeFragment;
import com.example.dhor.billigbytur.Fragments.MapFragment;
import com.example.dhor.billigbytur.Fragments.ProfileFragment;
import com.example.dhor.billigbytur.R;

/**
 * The activity that hosts the bottom navigation and the fragments of the homescreen.
 *
 * @author Thor Garske Andresen
 *
 * Date: 26/12/18.
 */
public class HomeScreenActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    // Debug variable.
    private static final String TAG = "HomeScreenActivity";

    private Fragment fragment;

    /**
     * Setups layout of the activity along with bottom navigation bar.
     * @param savedInstanceState A bundle to restore previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if (getSupportFragmentManager().getFragments().isEmpty()) {
            loadFragment(new HomeFragment());
        }

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation  = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Used for loading different fragments.
     * @param fragment The fragment to load.
     * @return True if load was successful, false otherwise.
     */
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Loads the corresponding fragments or activity when pressing items in navigation bar.
     * @param item The navigation item clicked.
     * @return Return true if fragment is loaded, false otherwise.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_map:
                fragment = new MapFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }

}
