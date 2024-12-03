package com.example.learn.presentation.ui.resto_detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.learn.data.dto.resto.CategoryDto;
import com.example.learn.data.dto.resto.GetProductsDto;
import com.example.learn.data.dto.resto.ProductDto;
import com.example.learn.data.dto.resto.ProductsPerCategory;
import com.example.learn.presentation.adapter.CategoryProductAdapter;
import com.example.learn.presentation.ui.widget.CustomActionBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestoDetailActivity extends AppCompatActivity {
    private RestoDetailViewModel viewModel;
    private View checkoutWrapper, checkoutBtn, sheetBtn;
    private ImageView restoAvatar, restoBanner, sheetProductImg;
    private TextView restoName, restoCategory, restoRating, restoRatingCount, totalQuantity, totalAmount, sheetProductName, sheetProductDesc, sheetProductSold, sheetProductLike, sheetProductPrice;
    private TabLayout categoryTab;
    private RecyclerView categoryProductRecycler;
    private CategoryProductAdapter categoryProductAdapter;
    private LinearLayoutManager categoryRecyclerLayoutManager;
    private BottomSheetDialog bottomSheetDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isTabClicked = false;
    private ProductDto selectedProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_detail);

        viewModel = new ViewModelProvider(this).get(RestoDetailViewModel.class);

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));

        restoBanner = findViewById(R.id.resto_banner);
        restoAvatar = findViewById(R.id.resto_avatar);
        restoName = findViewById(R.id.resto_name);
        restoCategory = findViewById(R.id.resto_category);
        restoRating = findViewById(R.id.resto_rating);
        restoRatingCount = findViewById(R.id.resto_rating_count);
        categoryTab = findViewById(R.id.category_tab);

        checkoutWrapper = findViewById(R.id.checkout_wrapper);
        checkoutBtn = findViewById(R.id.checkout_btn);
        totalQuantity = findViewById(R.id.total_quantity);
        totalAmount = findViewById(R.id.total_amount);

        categoryProductRecycler = findViewById(R.id.category_product_recycler);
        categoryRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryProductRecycler.setLayoutManager(categoryRecyclerLayoutManager);
        categoryProductRecycler.setNestedScrollingEnabled(false);

        categoryProductAdapter = new CategoryProductAdapter(this, viewModel, this::handleBottomSheet);
        categoryProductRecycler.setAdapter(categoryProductAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Glide.with(restoBanner.getContext()).load(extras.getString("banner")).placeholder(R.drawable.banner_home).into(restoBanner);
            Glide.with(restoAvatar.getContext()).load(extras.getString("avatar")).placeholder(R.drawable.img_placeholder).into(restoAvatar);
            restoName.setText(extras.getString("name"));
            restoCategory.setText(extras.getString("category"));
            restoRating.setText(String.format("%.1f", extras.getFloat("rating_avg")));
            restoRatingCount.setText(String.format("(%d)", extras.getInt("rating_count")));
        } else {
            finish();
        }

        initBottomSheet();
        listeners();
        stateObserver();
    }

    private void listeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.fetchProducts());
        checkoutBtn.setOnClickListener(v -> {
            Log.d("CHECKOUT", "clicked");
        });
    }

    private void stateObserver() {
        viewModel.getProducts().observe(this, productsState -> {
            switch (productsState.getStatus()) {
                case LOADING:
                    categoryProductAdapter.setLoading(true);
                    break;
                case SUCCESS:
                    categoryTab.removeAllTabs();
                    for (CategoryDto category: productsState.getData().meta.categories) {
                        categoryTab.addTab(categoryTab.newTab().setText(category.name));
                    }

                    ArrayList<ProductsPerCategory> productCategoryList = new ArrayList<>(Arrays.asList(productsState.getData().data));
                    categoryProductAdapter.setProductCategories(productCategoryList);
                    categoryTab.addOnTabSelectedListener(handleSelectCategoryTab());
                    categoryProductRecycler.addOnScrollListener(handleOnScrollCategoryRecycler());
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case ERROR:
                    Toast.makeText(this, productsState.getMessage(), Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        });

        viewModel.getQuantities().observe(this, quantities -> {
            int totalQuantities = viewModel.getTotalQuantities();

            if (totalQuantities > 0) {
                totalQuantity.setText(String.valueOf(totalQuantities));
                totalAmount.setText("Rp " + (new DecimalFormat("#,###")).format(viewModel.getTotalAmount()).replace(",", "."));
                checkoutWrapper.setVisibility(View.VISIBLE);
            } else {
                checkoutWrapper.setVisibility(View.GONE);
            }
        });
    }

    private void initBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(
                R.layout.layout_bottom_sheet,
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

        sheetProductImg = bottomSheetView.findViewById(R.id.sheet_product_img);
        sheetProductName = bottomSheetView.findViewById(R.id.sheet_product_name);
        sheetProductSold = bottomSheetView.findViewById(R.id.sheet_product_sold);
        sheetProductLike = bottomSheetView.findViewById(R.id.sheet_product_like);
        sheetProductPrice = bottomSheetView.findViewById(R.id.sheet_product_price);
        sheetProductDesc = bottomSheetView.findViewById(R.id.sheet_product_desc);
        sheetBtn = bottomSheetView.findViewById(R.id.sheet_add_btn);

        sheetBtn.setOnClickListener(v -> {
            if(selectedProduct != null) {
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
        Glide.with(this).load(product.image).into(sheetProductImg);
        sheetProductName.setText(product.name);
        sheetProductSold.setText(String.valueOf(product.sold));
        sheetProductLike.setText(String.valueOf(product.like));
        sheetProductPrice.setText("Rp " + (new DecimalFormat("#,###")).format(product.price).replace(",", "."));
        sheetProductDesc.setText(product.description);
        selectedProduct = product;

        bottomSheetDialog.show();
    }

    private RecyclerView.OnScrollListener handleOnScrollCategoryRecycler() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(!isTabClicked) {
                    int firstVisiblePosition = categoryRecyclerLayoutManager.findFirstVisibleItemPosition();
                    int cumulativeCount = 0;
                    GetProductsDto.Response categories = viewModel.getProducts().getValue().getData();

                    if(categories != null) {
                        for (int i = 0; i < categories.data.length; i++) {
                            cumulativeCount += categories.data[i].data.length;
                            if (firstVisiblePosition < cumulativeCount) {
                                categoryTab.selectTab(categoryTab.getTabAt(i));
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

                categoryProductRecycler.smoothScrollToPosition(startPosition);
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