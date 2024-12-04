package com.example.learn.presentation.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.learn.R;
import com.example.learn.presentation.adapter.TrxRestoAdapter;
import com.example.learn.presentation.interfaces.OnFragmentActionListener;
import com.example.learn.presentation.ui.home.HomeFragment;
import com.example.learn.presentation.ui.widget.CustomActionBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrxFragment extends Fragment {
    private TrxViewModel viewModel;
    private CustomActionBar actionBar;
    private RecyclerView trxRestoRecycler;
    private TrxRestoAdapter trxRestoAdapter;
    private OnFragmentActionListener listener;
    private Fragment homeFragment;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TrxFragment(Fragment homeFragment, BottomNavigationView bottomNavigationView) {
        this.homeFragment = homeFragment;
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trx, container, false);
        viewModel = new ViewModelProvider(this).get(TrxViewModel.class);

        actionBar = view.findViewById(R.id.action_bar);
        trxRestoRecycler = view.findViewById(R.id.trx_resto_recycler);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.primary));

        trxRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        trxRestoRecycler.setNestedScrollingEnabled(false);

        trxRestoAdapter = new TrxRestoAdapter(requireContext());
        trxRestoRecycler.setAdapter(trxRestoAdapter);

        listeners();
        stateObserver();

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

    private void listeners() {
        actionBar.getBackButton().setOnClickListener(v -> {
            listener.switchFragment(homeFragment);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.fetchGetTransactions());
    }

    private void stateObserver() {
        viewModel.getTransactionsState().observe(getViewLifecycleOwner(), transactionsState -> {
            switch (transactionsState.getStatus()) {
                case LOADING:
                    trxRestoAdapter.setLoading(true);
                    break;
                case SUCCESS:
                    trxRestoAdapter.setTransaction(Arrays.asList(transactionsState.getData().data));
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), transactionsState.getMessage(), Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        });
    }
}