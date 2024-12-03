package com.example.learn.presentation.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.presentation.adapter.TrxRestoAdapter;
import com.example.learn.presentation.interfaces.OnFragmentActionListener;
import com.example.learn.presentation.ui.home.HomeFragment;
import com.example.learn.presentation.ui.widget.CustomActionBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrxFragment extends Fragment {
    private TrxViewModel viewModel;
    private CustomActionBar actionBar;
    private RecyclerView trxRestoRecycler;
    private TrxRestoAdapter trxRestoAdapter;
    private OnFragmentActionListener listener;
    private Fragment homeFragment;
    private int itemId;
    private BottomNavigationView bottomNavigationView;

    public TrxFragment(Fragment homeFragment, BottomNavigationView bottomNavigationView, int itemId) {
        this.homeFragment = homeFragment;
        this.bottomNavigationView = bottomNavigationView;
        this.itemId = itemId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trx, container, false);
        viewModel = new ViewModelProvider(this).get(TrxViewModel.class);

        actionBar = view.findViewById(R.id.action_bar);
        trxRestoRecycler = view.findViewById(R.id.trx_resto_recycler);

        trxRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        trxRestoRecycler.setNestedScrollingEnabled(false);

        trxRestoAdapter = new TrxRestoAdapter(requireContext());
        trxRestoRecycler.setAdapter(trxRestoAdapter);

        listeners();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentActionListener) {
            listener = (OnFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentActionListener");
        }
    }

    public void listeners() {
        actionBar.getBackButton().setOnClickListener(v -> {
            listener.switchFragment(homeFragment);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        });
    }
}