package com.example.learn.presentation.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrxFragment extends Fragment {
    TrxViewModel viewModel;

    public TrxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trx, container, false);
        viewModel = new ViewModelProvider(this).get(TrxViewModel.class);

        return view;
    }
}