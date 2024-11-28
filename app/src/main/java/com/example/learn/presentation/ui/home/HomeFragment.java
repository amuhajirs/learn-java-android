package com.example.learn.presentation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.data.dto.resto.RestaurantsPerCategory;
import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;
import com.example.learn.presentation.adapter.CategoryRestoAdapter;
import com.example.learn.presentation.ui.login.LoginActivity;
import com.example.learn.presentation.ui.my_resto.MyRestoActivity;
import com.example.learn.presentation.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private ImageButton btnLogoutEl;
    private ImageView avatarEl;
    private RecyclerView categoryRestoRecycler;
    private CategoryRestoAdapter categoryRestoAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        btnLogoutEl = view.findViewById(R.id.logout_btn);
        avatarEl = view.findViewById(R.id.avatar);
        categoryRestoRecycler = view.findViewById(R.id.category_resto_recycler);

        String avatar = DataStoreSingleton.getInstance().getValueSync(DatastoreConst.USER_AVATAR);
        Glide.with(this)
            .load(avatar)
            .circleCrop()
            .into(avatarEl);

        categoryRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        categoryRestoRecycler.setNestedScrollingEnabled(false);

        listeners();
        stateObserver();

        return view;
    }

    public void listeners() {
        btnLogoutEl.setOnClickListener(this::handleLogout);
        avatarEl.setOnClickListener(this::handleClickAvatar);
    }

    public void stateObserver() {
        viewModel.getLogoutSuccessMsg().observe(getViewLifecycleOwner(), msg -> {
            DataStoreSingleton.getInstance().saveValue(DatastoreConst.ACC_TOKEN, "");
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            getActivity().finish();
        });

        viewModel.getLogoutErrorMsg().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            getActivity().finish();
        });

        viewModel.getRestos().observe(getViewLifecycleOwner(), restos -> {
            if (restos != null) {
                ArrayList<RestaurantsPerCategory> restoCategoryList = new ArrayList<>();
                Collections.addAll(restoCategoryList, restos.data);
                categoryRestoAdapter = new CategoryRestoAdapter(requireContext(), restoCategoryList);
                categoryRestoRecycler.setAdapter(categoryRestoAdapter);
            }
        });

        viewModel.getErrorMsgGetResto().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
        });
    }

    public void handleLogout(View v) {
        v.setEnabled(false);

        viewModel.logout();
    }

    public void handleClickAvatar(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);

        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.user_option_menu, popupMenu.getMenu());

        // Set menu item click listener
        popupMenu.setOnMenuItemClickListener(this::handleMenuItemClick);

        // Show the PopupMenu
        popupMenu.show();
    }

    private boolean handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.settings) {
            Intent intent = new Intent(requireContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_resto) {
            Intent intent = new Intent(requireContext(), MyRestoActivity.class);
            startActivity(intent);
        } else {
            return false;
        }

        return true;
    }
}