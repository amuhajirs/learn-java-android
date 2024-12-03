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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        btnLogoutEl = view.findViewById(R.id.logout_btn);
        avatarEl = view.findViewById(R.id.avatar);
        categoryRestoRecycler = view.findViewById(R.id.category_resto_recycler);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.primary));

        String avatar = DataStoreSingleton.getInstance().getValueSync(DatastoreConst.USER_AVATAR);
        Glide.with(this)
            .load(avatar)
            .circleCrop()
            .placeholder(R.drawable.img_placeholder)
            .into(avatarEl);

        categoryRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        categoryRestoRecycler.setNestedScrollingEnabled(false);

        categoryRestoAdapter = new CategoryRestoAdapter(requireContext());
        categoryRestoRecycler.setAdapter(categoryRestoAdapter);

        listeners();
        stateObserver();

        return view;
    }

    private void listeners() {
        btnLogoutEl.setOnClickListener(this::handleLogout);
        avatarEl.setOnClickListener(this::handleClickAvatar);
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.fetchRestos());
    }

    private void stateObserver() {
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

        viewModel.getRestosState().observe(getViewLifecycleOwner(), restosState -> {
            switch (restosState.getStatus()) {
                case LOADING:
                    categoryRestoAdapter.setLoading(true);
                    break;
                case SUCCESS:
                    ArrayList<RestaurantsPerCategory> restoCategoryList = new ArrayList<>();
                    Collections.addAll(restoCategoryList, restosState.getData().data);
                    categoryRestoAdapter.setRestoCategories(restoCategoryList);
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), restosState.getMessage(), Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        });
    }

    private void handleLogout(View v) {
        v.setEnabled(false);

        viewModel.logout();
    }

    private void handleClickAvatar(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.user_option_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(this::handleMenuItemClick);
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