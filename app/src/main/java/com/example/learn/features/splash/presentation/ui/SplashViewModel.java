package com.example.learn.features.splash.presentation.ui;

import androidx.lifecycle.ViewModel;

import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;
import com.example.learn.features.auth.data.dto.GetProfileDto;
import com.example.learn.features.auth.domain.usecase.GetProfileUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashViewModel extends ViewModel {
    public final GetProfileUseCase getProfileUseCase;

    @Inject
    SplashViewModel(GetProfileUseCase getProfileUseCase) {
        this.getProfileUseCase = getProfileUseCase;
    }

    public void checkAuth(CheckAuthCb cb) {
        DataStoreSingleton.getInstance().getValue(DatastoreConst.ACC_TOKEN, s -> {
            if (s == null || s.isEmpty()) {
                cb.onGuest();
            } else {
                getProfileUseCase.execute(cb);
            }
        });
    }

    public interface CheckAuthCb {
        void onAuthenticated(GetProfileDto response);

        void onGuest();
    }
}
