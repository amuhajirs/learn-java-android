package com.example.learn.features.transactions.presentation.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.learn.R;
import com.example.learn.common.utils.ArrayUtils;
import com.example.learn.features.transactions.domain.model.Filter;
import com.example.learn.features.transactions.domain.model.FilterTransactions;
import com.example.learn.features.transactions.presentation.adapter.TrxRestoAdapter;
import com.example.learn.features.main.presentation.interfaces.OnFragmentActionListener;
import com.example.learn.shared.presentation.widget.CustomActionBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrxFragment extends Fragment {
    private TrxViewModel viewModel;
    private View filterStatus, filterCategory, filterDate, clearBtn;
    private CustomActionBar actionBar;
    private RecyclerView trxRestoRecycler;
    private TrxRestoAdapter trxRestoAdapter;
    private OnFragmentActionListener listener;
    private Fragment homeFragment;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton closeStatusDialog;
    private BottomSheetDialog bottomSheetDialog;
    private RadioGroup sheetFilterGroup;
    private TextView sheetTitle, filterStatusLabel, filterCategoryLabel, filterDateLabel;
    private ImageView filterStatusIcon, filterCategoryIcon, filterDateIcon;

    private final Filter[] STATUS = {
            new Filter("Semua Status", ""),
            new Filter("Menunggu Konfirmasi", "WAITING"),
            new Filter("Diproses", "INPROGRESS"),
            new Filter("Siap Diambil", "READY"),
            new Filter("Sukses", "SUCCESS"),
            new Filter("Dibatalkan", "CANCELED"),
    };
    private final Filter[] CATEGORY = {
            new Filter("Semua Kategori", ""),
            new Filter("Makanan", "1"),
            new Filter("Minuman", "2"),
            new Filter("Jajanan", "3"),
    };

    private final Filter[] DATE = {
            new Filter("Semua Tanggal", ""),
            new Filter("30 Hari Terakhir", "1"),
            new Filter("90 Hari Terakhir", "2"),
    };

    public TrxFragment() {
    }

    public static TrxFragment newInstance(Fragment homeFragment, BottomNavigationView bottomNavigationView) {
        TrxFragment fragment = new TrxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.homeFragment = homeFragment;
        fragment.bottomNavigationView = bottomNavigationView;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trx, container, false);
        viewModel = new ViewModelProvider(this).get(TrxViewModel.class);

        actionBar = view.findViewById(R.id.action_bar);
        trxRestoRecycler = view.findViewById(R.id.trx_resto_recycler);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

        clearBtn = view.findViewById(R.id.clear_btn);
        filterStatus = view.findViewById(R.id.filter_status);
        filterCategory = view.findViewById(R.id.filter_category);
        filterDate = view.findViewById(R.id.filter_date);

        filterStatusLabel = filterStatus.findViewById(R.id.filter_status_label);
        filterStatusIcon = filterStatus.findViewById(R.id.filter_status_icon);
        filterCategoryLabel = filterCategory.findViewById(R.id.filter_category_label);
        filterCategoryIcon = filterCategory.findViewById(R.id.filter_category_icon);
        filterDateLabel = filterDate.findViewById(R.id.filter_date_label);
        filterDateIcon = filterDate.findViewById(R.id.filter_date_icon);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.primary));

        trxRestoRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        trxRestoRecycler.setNestedScrollingEnabled(false);

        trxRestoAdapter = new TrxRestoAdapter(requireContext());
        trxRestoRecycler.setAdapter(trxRestoAdapter);

        initBottomSheet();

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

        actionBar.getSearchInput().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = actionBar.getSearchInput().getText().toString();
                viewModel.updateSearchFilter(query);

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.fetchGetTransactions());

        filterStatus.setOnClickListener(v -> renderRadioFilter("Mau lihat status apa?", STATUS, R.id.filter_status));
        filterCategory.setOnClickListener(v -> renderRadioFilter("Mau lihat kategori apa?", CATEGORY, R.id.filter_category));
        filterDate.setOnClickListener(v -> renderRadioFilter("Pilih tanggal", DATE, R.id.filter_date));

        sheetFilterGroup.setOnCheckedChangeListener(this::handleChangeSelectedFilter);
        clearBtn.setOnClickListener(v -> {
            viewModel.clearFilter();
            setNotActiveFilter(filterStatus, filterStatusLabel, filterStatusIcon);
            setNotActiveFilter(filterCategory, filterCategoryLabel, filterCategoryIcon);
            setNotActiveFilter(filterDate, filterDateLabel, filterDateIcon);

            filterStatusLabel.setText(STATUS[0].label);
            filterCategoryLabel.setText(CATEGORY[0].label);
            filterDateLabel.setText(DATE[0].label);
        });
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

        viewModel.getFilterTrx().observe(getViewLifecycleOwner(), filterTrx -> {
            if(!filterTrx.status.isEmpty() || !filterTrx.categoryId.isEmpty() || !filterTrx.date.isEmpty()) {
                clearBtn.setVisibility(View.VISIBLE);
            } else {
                clearBtn.setVisibility(View.GONE);
            }

            viewModel.fetchGetTransactions();
        });
    }

    private void initBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                R.layout.bottom_sheet_filter,
                null
        );

        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.bottom_sheet);
            }
        });

        sheetTitle = bottomSheetView.findViewById(R.id.sheet_title);
        sheetFilterGroup = bottomSheetView.findViewById(R.id.filter_group);

        closeStatusDialog = bottomSheetView.findViewById(R.id.status_close_btn);
        closeStatusDialog.setOnClickListener(v -> bottomSheetDialog.hide());
    }

    private void renderRadioFilter(String title, Filter[] filters, int id) {
        sheetFilterGroup.removeAllViews();
        sheetFilterGroup.setTag(id);
        sheetTitle.setText(title);

        FilterTransactions currentFilter = viewModel.getFilterTrx().getValue();

        for (Filter filter: filters) {
            View radioButtonView = getLayoutInflater().from(requireContext()).inflate(R.layout.radio_button, sheetFilterGroup, false);

            RadioButton radioButton = radioButtonView.findViewById(R.id.radio_button);
            radioButton.setText(filter.label);
            radioButton.setTag(filter.value);
            radioButton.setId(View.generateViewId());

            if (id == R.id.filter_status) {
                if(filter.value.equals(currentFilter.status)) {
                    radioButton.setChecked(true);
                }
            } else if (id == R.id.filter_category) {
                if(filter.value.equals(currentFilter.categoryId)) {
                    radioButton.setChecked(true);
                }
            } else {
                if(filter.value.equals(currentFilter.date)) {
                    radioButton.setChecked(true);
                }
            }

            sheetFilterGroup.addView(radioButton);
        }

        bottomSheetDialog.show();
    }

    private void handleChangeSelectedFilter(RadioGroup group, int checkedId) {
        RadioButton selectedRadioButton = group.findViewById(checkedId);
        if (selectedRadioButton != null) {
            String value = selectedRadioButton.getTag().toString();

            if(group.getTag().toString().equals(String.valueOf(R.id.filter_status))) {
                viewModel.updateStatusFilter(value);

                filterStatusLabel.setText(ArrayUtils.find(STATUS, item -> item.value == value).label);

                if (value.isEmpty()) {
                    setNotActiveFilter(filterStatus, filterStatusLabel, filterStatusIcon);
                } else {
                    setActiveFilter(filterStatus, filterStatusLabel, filterStatusIcon);
                }
            } else if (group.getTag().toString().equals(String.valueOf(R.id.filter_category))) {
                viewModel.updateCategoryFilter(value);

                filterCategoryLabel.setText(ArrayUtils.find(CATEGORY, item -> item.value == value).label);

                if (value.isEmpty()) {
                    setNotActiveFilter(filterCategory, filterCategoryLabel, filterCategoryIcon);
                } else {
                    setActiveFilter(filterCategory, filterCategoryLabel, filterCategoryIcon);
                }
            } else {
                viewModel.updateDateFilter(value);

                filterDateLabel.setText(ArrayUtils.find(DATE, item -> item.value == value).label);

                if (value.isEmpty()) {
                    setNotActiveFilter(filterDate, filterDateLabel, filterDateIcon);
                } else {
                    setActiveFilter(filterDate, filterDateLabel, filterDateIcon);
                }
            }
        }

        bottomSheetDialog.hide();
    }

    private void setActiveFilter(View cardView, TextView label, ImageView icon) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50);
        drawable.setStroke(4, ContextCompat.getColor(requireContext(), R.color.primary)); // Border (ketebalan dan warna)
        drawable.setColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(requireContext(), R.color.primary), (int) (0.2 * 255)));

        cardView.setBackground(drawable);
        label.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
        icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary), PorterDuff.Mode.SRC_IN);
    }

    private void setNotActiveFilter(View cardView, TextView label, ImageView icon) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50);
        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.secondary));

        cardView.setBackground(drawable);
        label.setTextColor(ContextCompat.getColor(requireContext(), R.color.text));
        icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.text), PorterDuff.Mode.SRC_IN);
    }
}