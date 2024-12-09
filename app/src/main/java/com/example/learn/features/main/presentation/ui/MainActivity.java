package com.example.learn.features.main.presentation.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;
import com.example.learn.features.home.presentation.ui.HomeFragment;
import com.example.learn.features.main.presentation.interfaces.OnFragmentActionListener;
import com.example.learn.features.transactions.presentation.ui.TrxFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnFragmentActionListener {
    private BottomNavigationView bottomNavigationView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fab.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
        bottomNavigationView.setBackground(null);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments.size() == 1) {
            viewModel.setActiveFragment(fragments.get(0));
        }

        for (Fragment fragment : fragments) {
            if (fragment instanceof HomeFragment) {
                viewModel.putFragment("Home", fragment);
            } else {
                viewModel.putFragment("Transactions", fragment);
            }

            Fragment activeFragment = viewModel.getActiveFragment();
            if (activeFragment != null && activeFragment.getClass().equals(fragment.getClass())) {
                viewModel.setActiveFragment(fragment);
            }
        }

        listeners();
    }

    public void listeners() {
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
    }

    public boolean handleBottomNav(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            if (viewModel.getFragment("Home") == null) {
                viewModel.putFragment("Home", new HomeFragment());
            }
            switchFragment(viewModel.getFragment("Home"));
            return true;
        } else if (itemId == R.id.nav_transaction) {
            if (viewModel.getFragment("Transactions") == null) {
                viewModel.putFragment("Transactions", TrxFragment.newInstance(viewModel.getFragment("Home"), bottomNavigationView));
            }
            switchFragment(viewModel.getFragment("Transactions"));
            return true;
        }
        return false;
    }

    @Override
    public void switchFragment(Fragment targetFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.hide(viewModel.getActiveFragment());

        if (targetFragment.isAdded()) {
            fragmentTransaction.show(targetFragment);
        } else {
            fragmentTransaction.add(R.id.fragmentContainerView, targetFragment);
        }

        viewModel.setActiveFragment(targetFragment);

        fragmentTransaction.commit();
    }

}