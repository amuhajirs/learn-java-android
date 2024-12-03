package com.example.learn.presentation.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.learn.R;
import com.example.learn.presentation.interfaces.OnFragmentActionListener;
import com.example.learn.presentation.ui.home.HomeFragment;
import com.example.learn.presentation.ui.transaction.TrxFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnFragmentActionListener {
    private BottomNavigationView bottomNavigationView;
    private Fragment homeFragment, trxFragment, activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fab.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
        bottomNavigationView.setBackground(null);
        homeFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        activeFragment = homeFragment;

        listeners();
    }

    public void listeners() {
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
    }

    public boolean handleBottomNav(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            switchFragment(homeFragment);
            return true;
        } else if (itemId == R.id.nav_transaction) {
            if(trxFragment == null) {
                trxFragment = new TrxFragment(homeFragment, bottomNavigationView, itemId);
            }
            switchFragment(trxFragment);
            return true;
        }
        return false;
    }

    @Override
    public void switchFragment(Fragment targetFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.hide(activeFragment);

        if (targetFragment.isAdded()) {
            fragmentTransaction.show(targetFragment);
        } else {
            fragmentTransaction.add(R.id.fragmentContainerView, targetFragment, targetFragment.getClass().getSimpleName());
        }

        activeFragment = targetFragment;

        fragmentTransaction.commit();
    }

}