package com.example.learn.features.resto.presentation.ui;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.common.utils.StringUtils;
import com.example.learn.databinding.ActivityRestoDetailBinding;
import com.example.learn.databinding.BottomSheetRestoDetailBinding;
import com.example.learn.features.resto.data.dto.CategoryDto;
import com.example.learn.features.resto.data.dto.GetProductsDto;
import com.example.learn.features.resto.data.dto.ProductDto;
import com.example.learn.features.resto.presentation.adapter.CategoryProductAdapter;
import com.example.learn.shared.presentation.widget.CustomActionBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestoDetailActivity extends AppCompatActivity {
    private ActivityRestoDetailBinding binding;
    private BottomSheetRestoDetailBinding bottomSheetBinding;
    private RestoDetailViewModel viewModel;
    private CategoryProductAdapter categoryProductAdapter;
    private LinearLayoutManager categoryRecyclerLayoutManager;
    private BottomSheetDialog bottomSheetDialog;
    private boolean isTabClicked = false;
    private ProductDto selectedProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRestoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RestoDetailViewModel.class);

        binding.refresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));

        categoryRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.categoryProductRecycler.setLayoutManager(categoryRecyclerLayoutManager);
        binding.categoryProductRecycler.setNestedScrollingEnabled(false);

        categoryProductAdapter = new CategoryProductAdapter(this, viewModel, this::handleBottomSheet);
        binding.categoryProductRecycler.setAdapter(categoryProductAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Glide.with(binding.restoBanner.getContext()).load(extras.getString("banner")).placeholder(R.drawable.banner_home).into(binding.restoBanner);
            Glide.with(binding.restoAvatar.getContext()).load(extras.getString("avatar")).placeholder(R.drawable.img_placeholder).into(binding.restoAvatar);
            binding.restoName.setText(extras.getString("name"));
            binding.restoCategory.setText(extras.getString("category"));
            binding.restoRating.setText(String.format("%.1f", extras.getFloat("rating_avg")));
            binding.restoRatingCount.setText(String.format("(%d)", extras.getInt("rating_count")));
        } else {
            finish();
        }

        initBottomSheet();
        listeners();
        stateObserver();
    }

    private void listeners() {
        binding.refresh.setOnRefreshListener(() -> viewModel.fetchProducts());
        binding.checkoutBtn.setOnClickListener(v -> viewModel.fetchCheckout());
        binding.actionBar.getSearchInput().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = binding.actionBar.getSearchInput().getText().toString();
                viewModel.updateSearchQuery(query);

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
    }

    private void stateObserver() {
        viewModel.getProducts().observe(this, productsState -> {
            switch (productsState.getStatus()) {
                case LOADING:
                    categoryProductAdapter.setLoading(true);
                    break;
                case SUCCESS:
                    binding.categoryTab.removeAllTabs();
                    for (CategoryDto category : productsState.getData().meta.categories) {
                        binding.categoryTab.addTab(binding.categoryTab.newTab().setText(category.name));
                    }

                    categoryProductAdapter.setProductCategories(Arrays.asList(productsState.getData().data));
                    binding.categoryTab.addOnTabSelectedListener(handleSelectCategoryTab());
                    binding.categoryProductRecycler.addOnScrollListener(handleOnScrollCategoryRecycler());
                    binding.refresh.setRefreshing(false);
                    break;
                case ERROR:
                    Toast.makeText(this, productsState.getMessage(), Toast.LENGTH_LONG).show();
                    binding.refresh.setRefreshing(false);
                    break;
            }
        });

        viewModel.getQuantities().observe(this, quantities -> {
            int totalQuantities = viewModel.getTotalQuantities();

            if (totalQuantities > 0) {
                binding.totalQuantity.setText(String.valueOf(totalQuantities));
                binding.totalAmount.setText(StringUtils.formatCurrency(viewModel.getTotalAmount()));
                binding.checkoutWrapper.setVisibility(View.VISIBLE);
            } else {
                binding.checkoutWrapper.setVisibility(View.GONE);
            }
        });

        viewModel.getCheckoutState().observe(this, checkoutState -> {
            switch (checkoutState.getStatus()) {
                case LOADING:
                    binding.checkoutBtn.setEnabled(false);
                    break;
                case SUCCESS:
                    binding.checkoutBtn.setEnabled(true);
                    Toast.makeText(this, checkoutState.getData().message, Toast.LENGTH_LONG).show();
                    break;
                case ERROR:
                    binding.checkoutBtn.setEnabled(true);
                    Toast.makeText(this, checkoutState.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });

        viewModel.getQuery().observe(this, query -> {
            viewModel.fetchProducts();
        });
    }

    private void initBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetBinding = BottomSheetRestoDetailBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.bottom_sheet);
            }
        });

        bottomSheetBinding.sheetAddBtn.setOnClickListener(v -> {
            if (selectedProduct != null) {
                int currentQty = viewModel.getQuantities().getValue().getOrDefault(selectedProduct.id, 0);
                if (currentQty < selectedProduct.stock) {
                    viewModel.updateQuantity(selectedProduct.id, currentQty + 1);
                    selectedProduct = null;
                    bottomSheetDialog.hide();
                }
            }
        });
    }

    public void handleBottomSheet(ProductDto product) {
        Glide.with(this).load(product.image).into(bottomSheetBinding.sheetProductImg);
        bottomSheetBinding.sheetProductName.setText(product.name);
        bottomSheetBinding.sheetProductSold.setText(String.valueOf(product.sold));
        bottomSheetBinding.sheetProductLike.setText(String.valueOf(product.like));
        bottomSheetBinding.sheetProductPrice.setText(StringUtils.formatCurrency(product.price));
        bottomSheetBinding.sheetProductDesc.setText(product.description);
        selectedProduct = product;

        bottomSheetDialog.show();
    }

    private RecyclerView.OnScrollListener handleOnScrollCategoryRecycler() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isTabClicked) {
                    int firstVisiblePosition = categoryRecyclerLayoutManager.findFirstVisibleItemPosition();
                    int cumulativeCount = 0;
                    GetProductsDto.Response categories = viewModel.getProducts().getValue().getData();

                    if (categories != null) {
                        for (int i = 0; i < categories.data.length; i++) {
                            cumulativeCount += categories.data[i].data.length;
                            if (firstVisiblePosition < cumulativeCount) {
                                binding.categoryTab.selectTab(binding.categoryTab.getTabAt(i));
                                break;
                            }
                        }
                    }

                }
            }
        };
    }

    private TabLayout.OnTabSelectedListener handleSelectCategoryTab() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isTabClicked = true;
                int position = tab.getPosition();
                int startPosition = 0;

                for (int i = 0; i < position; i++) {
                    startPosition += viewModel.getProducts().getValue().getData().data[i].data.length;
                }

                binding.categoryProductRecycler.smoothScrollToPosition(startPosition);
                isTabClicked = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
}