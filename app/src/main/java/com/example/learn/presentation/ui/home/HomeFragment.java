package com.example.learn.presentation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.data.dto.resto.RestaurantsPerCategory;
import com.example.learn.presentation.adapter.CategoryRestoAdapter;
import com.example.learn.presentation.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private ImageButton btnLogoutEl;
    private RecyclerView categoryRestoRecycler;
    private CategoryRestoAdapter categoryRestoAdapter;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        btnLogoutEl = view.findViewById(R.id.logout_btn);
        categoryRestoRecycler = view.findViewById(R.id.category_resto_recycler);

        categoryRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        categoryRestoRecycler.setNestedScrollingEnabled(false);

        listeners();
        stateObserver();

        return view;
    }

    public void listeners() {
        btnLogoutEl.setOnClickListener(this::handleLogout);
    }

    public void stateObserver() {
        viewModel.getLogoutSuccessMsg().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        viewModel.getLogoutErrorMsg().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            btnLogoutEl.setEnabled(true);
        });

        viewModel.getRestos().observe(getViewLifecycleOwner(), restos -> {
            if (restos != null) {
                ArrayList<RestaurantsPerCategory> restoCategoryList = new ArrayList<>();
                Collections.addAll(restoCategoryList, restos.data);
                handleGetRestos(restoCategoryList);
            }
        });
    }

    public void handleGetRestos(List<RestaurantsPerCategory> restoCategories) {
        categoryRestoAdapter = new CategoryRestoAdapter(getContext(), restoCategories);

        categoryRestoRecycler.setAdapter(categoryRestoAdapter);
    }

    public void handleLogout(View v) {
        v.setEnabled(false);

        viewModel.logout();
    }
}