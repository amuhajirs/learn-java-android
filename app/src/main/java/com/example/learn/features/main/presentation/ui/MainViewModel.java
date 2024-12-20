package com.example.learn.features.main.presentation.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final MutableLiveData<Fragment> activeFragment = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Fragment>> fragments = new MutableLiveData<>(new HashMap<>());

    @Inject
    public MainViewModel() {
    }

    public Fragment getActiveFragment() {
        return activeFragment.getValue();
    }

    public void setActiveFragment(Fragment fragment) {
        activeFragment.setValue(fragment);
    }

    public Fragment getFragment(String key) {
        return fragments.getValue().get(key);
    }

    public void putFragment(String key, Fragment fragment) {
        Map<String, Fragment> currentFragments = fragments.getValue();
        currentFragments.put(key, fragment);
        fragments.setValue(currentFragments);
    }
}
