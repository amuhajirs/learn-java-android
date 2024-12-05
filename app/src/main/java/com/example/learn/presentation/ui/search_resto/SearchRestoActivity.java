package com.example.learn.presentation.ui.search_resto;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learn.R;
import com.example.learn.presentation.ui.widget.CustomActionBar;

public class SearchRestoActivity extends AppCompatActivity {
    private CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_resto);

        actionBar = findViewById(R.id.action_bar);
        actionBar.getSearchInput().requestFocus();

        actionBar.getSearchInput().postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(actionBar.getSearchInput(), InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);
    }
}