package com.example.learn.features.home.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;
import com.example.learn.databinding.FragmentHomeBinding;
import com.example.learn.features.auth.presentation.ui.LoginActivity;
import com.example.learn.features.home.presentation.adapter.CategoryRestoAdapter;
import com.example.learn.features.my_resto.presentation.ui.MyRestoActivity;
import com.example.learn.features.search.presentation.ui.SearchRestoActivity;
import com.example.learn.features.settings.presentation.ui.SettingsActivity;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private CategoryRestoAdapter categoryRestoAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.refresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.primary));

        String avatar = DataStoreSingleton.getInstance().getValueSync(DatastoreConst.USER_AVATAR);
        Glide.with(this)
                .load(avatar)
                .circleCrop()
                .placeholder(R.drawable.img_placeholder)
                .into(binding.avatar);

        binding.categoryRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.categoryRestoRecycler.setNestedScrollingEnabled(false);

        categoryRestoAdapter = new CategoryRestoAdapter(getActivity());
        binding.categoryRestoRecycler.setAdapter(categoryRestoAdapter);

        listeners();
        stateObserver();

        return binding.getRoot();
    }

    private void listeners() {
        binding.logoutBtn.setOnClickListener(this::handleLogout);
        binding.avatar.setOnClickListener(this::handleClickAvatar);
        binding.refresh.setOnRefreshListener(() -> viewModel.fetchRestos());
        binding.searchInput.setOnClickListener((v) -> {
            startActivity(new Intent(requireContext(), SearchRestoActivity.class));
            getActivity().overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);
        });
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
                    categoryRestoAdapter.setRestoCategories(Arrays.asList(restosState.getData().data));
                    binding.refresh.setRefreshing(false);
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), restosState.getMessage(), Toast.LENGTH_LONG).show();
                    binding.refresh.setRefreshing(false);
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

        if (id == R.id.settings) {
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